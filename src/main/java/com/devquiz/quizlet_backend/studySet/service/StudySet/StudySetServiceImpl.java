package com.devquiz.quizlet_backend.studySet.service.StudySet;

import com.devquiz.quizlet_backend.card.dto.request.CardRequest;
import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.card.repository.CardRepository;
import com.devquiz.quizlet_backend.learn.dto.request.LearningRequest;
import com.devquiz.quizlet_backend.learn.dto.response.MatchQuizzResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizQuestionResponse;
import com.devquiz.quizlet_backend.learn.dto.response.QuizzDataReponse;
import com.devquiz.quizlet_backend.learn.entity.LearningProcess;
import com.devquiz.quizlet_backend.learn.repository.LearningProgressRepository;
import com.devquiz.quizlet_backend.learn.types.LearningStatus;
import com.devquiz.quizlet_backend.studySet.dto.request.SavedStudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.dto.response.TestQuestionDto;
import com.devquiz.quizlet_backend.studySet.dto.response.TestType;
import com.devquiz.quizlet_backend.studySet.entity.SavedStudySet;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.repository.SavedStudySetRepository;
import com.devquiz.quizlet_backend.studySet.repository.StudySetRepository;
import com.devquiz.quizlet_backend.studySet.service.S3Service.S3Service;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository studySetRepository;
    private final UserRepository userRepository;
    private final SavedStudySetRepository savedStudySetRepository;
    private final LearningProgressRepository learningProgressRepository;
    private final CardRepository cardRepository;
    private final S3Service s3Service;

    private void validateRequest(StudySetRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        if (request.getCards() == null || request.getCards().isEmpty())
            throw new IllegalArgumentException("Study set must have at least one card");
    }

    @Transactional
    public String toogleSaveSet(SavedStudySetRequest savedStudySetRequest, Long studySetId) {
        System.out.println("Toggle save called with email: " + savedStudySetRequest.getUserEmail() + " and studySetId: " + studySetId);
        User user = userRepository.findByEmail(savedStudySetRequest.getUserEmail()).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(
                () -> new IllegalArgumentException("Study set not found")
        );

        Optional<SavedStudySet> existing = savedStudySetRepository.findByUserAndStudySet(user, studySet);
        if (existing.isPresent()) {
            savedStudySetRepository.delete(existing.get());
            return "Study set unsaved successfully";
        } else {
            SavedStudySet savedStudySet = new SavedStudySet();
            savedStudySet.setUser(user);
            savedStudySet.setStudySet(studySet);
            savedStudySetRepository.save(savedStudySet);
            return "Study set saved successfully";
        }
    }

    @Override
    public List<StudySetResponse> getMySavedSets(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        List<SavedStudySet> savedSet = savedStudySetRepository.findAllByUserId(user.getUserId());

        return savedSet.stream().map(
                savedStudySet -> getStudySetResponse(savedStudySet.getStudySet())
        ).collect(Collectors.toList()
        );
    }


    @Override
    public QuizzDataReponse generateQuiz(Long studySetId, Long userId) {
        // 1. Tìm bộ thẻ
        StudySet set = studySetRepository.findById(studySetId)
                .orElseThrow(() -> new IllegalArgumentException("Study set not found"));

        List<Card> allCards = set.getCards();
        if (allCards.isEmpty()) {
            throw new IllegalArgumentException("Bộ thẻ này không có dữ liệu!");
        }

        // 2. Lấy danh sách ID thẻ theo trạng thái của User
        // Lấy thẻ đã thuộc (để loại bỏ)
        List<Long> masteredCardIds = learningProgressRepository.findCardIdsByStatus(
                userId, studySetId, LearningStatus.MASTERED);

        // Lấy thẻ ĐANG HỌC (những câu từng làm sai hoặc chưa xong - đây chính là nguồn của "Try Again")
        List<Long> learningCardIds = learningProgressRepository.findCardIdsByStatus(
                userId, studySetId, LearningStatus.LEARNING);

        // 3. LỌC THÔNG MINH:
        // Ưu tiên những thẻ 'LEARNING' (cần làm lại), sau đó mới đến thẻ 'NEW' (chưa học bao giờ)
        List<Card> cardsToLearn = allCards.stream()
                .filter(card -> !masteredCardIds.contains(card.getCardId()))
                .sorted((c1, c2) -> {
                    // Ưu tiên thẻ đang học (LEARNING) lên đầu để User làm lại cho thuộc
                    boolean c1IsLearning = learningCardIds.contains(c1.getCardId());
                    boolean c2IsLearning = learningCardIds.contains(c2.getCardId());
                    return Boolean.compare(c2IsLearning, c1IsLearning);
                })
                .limit(10) // Giới hạn số lượng thẻ mỗi lượt học (giống Quizlet thường học theo từng cụm)
                .toList();

        if (cardsToLearn.isEmpty()) {
            return QuizzDataReponse.builder()
                    .questions(Collections.emptyList())
                    .totalCards(allCards.size())
                    .masteredCount(masteredCardIds.size())
                    .initialPercentage(100.0)
                    .build();
        }

        // 4. Tạo danh sách câu hỏi với options xáo trộn
        List<QuizQuestionResponse> questions = cardsToLearn.stream().map(currentCard -> {
            List<String> distractors = allCards.stream()
                    .filter(c -> !c.getCardId().equals(currentCard.getCardId()))
                    .map(Card::getDefinition)
                    .collect(Collectors.toList());

            Collections.shuffle(distractors);
            List<String> options = new ArrayList<>(distractors.subList(0, Math.min(3, distractors.size())));
            options.add(currentCard.getDefinition());
            Collections.shuffle(options);

            String status = learningCardIds.contains(currentCard.getCardId()) ? "LEARNING" : "NEW";

            return QuizQuestionResponse.builder()
                    .cardId(currentCard.getCardId())
                    .term(currentCard.getTerm())
                    .definition(currentCard.getDefinition())
                    .options(options)
                    .cardStatus(status) // Gán status vào đây <---
                    .build();
        }).collect(Collectors.toList());

        // 5. Tính toán tiến độ dựa trên số lượng MASTERED thực tế trong DB
        double percentage = ((double) masteredCardIds.size() / allCards.size()) * 100;

        return QuizzDataReponse.builder()
                .questions(questions)
                .totalCards(allCards.size())
                .masteredCount(masteredCardIds.size())
                .initialPercentage(percentage)
                .build();
    }

    @Override
    public List<CardResponse> getMasteredCards(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        List<StudySet> masteredSets = studySetRepository.findAllMasteredStudySetsByUserEmail(userEmail);

//        List<Card> masterCard =  cardRepository.findByStudySetId(studySetId).
        return masteredSets.stream().flatMap(set -> set.getCards().stream())
                .filter(card -> learningProgressRepository.findByUserAndCard(user, card)
                        .map(lp -> lp.getStatus() == LearningStatus.MASTERED)
                        .orElse(false))
                .map(card -> new CardResponse(
                        card.getCardId(),
                        card.getTerm(),
                        card.getDefinition(),
                        Optional.ofNullable(card.getCardImage()),
                        Optional.of("MASTERED")
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<MatchQuizzResponse> generateMatchQuiz(Long studySetId, Long userId) {
        StudySet studySet = studySetRepository.findById(studySetId)
                .orElseThrow(() -> new IllegalArgumentException("Study set not found"));
        List<Card> allCards = studySet.getCards();
        if (allCards.isEmpty()) {
            throw new IllegalArgumentException("Bộ thẻ này không có dữ liệu!");
        }
        List<Card> randomCards = new ArrayList<>(allCards);
        Collections.shuffle(randomCards);
        List<Card> selectedCards = randomCards.subList(0, Math.min(6, randomCards.size()));
        List<MatchQuizzResponse> gamePieces = new ArrayList<>();
        for (Card card : selectedCards) {
            gamePieces.add(MatchQuizzResponse.builder().cardId(card.getCardId())
                    .term(card.getTerm()).type("TERM")
                    .build());
            gamePieces.add(
                    MatchQuizzResponse.builder().cardId(card.getCardId())
                            .definition(card.getDefinition()).type("DEFINITION")
                            .build()
            );
        }
        Collections.shuffle(selectedCards);
        return gamePieces;
    }


    @Override
// Logic gợi ý trong StudySetService
    public List<CardResponse> getCardsWithStatus(String userEmail, Long studySetId) {
        // 1. Lấy tất cả thẻ của bộ đó
        List<Card> cards = cardRepository.findByStudySetId(studySetId);
        // 2. Lấy tiến độ học của User này cho bộ thẻ này
        List<LearningProcess> processes = learningProgressRepository.findAllByUserId(userEmail);
        // 3. Map sang DTO
        return cards.stream().map(card -> {
            // Tìm xem thẻ này có trong bảng tiến độ chưa
            String status = processes.stream()
                    .filter(p -> p.getCard().getCardId().equals(card.getCardId()))
                    .map(p -> p.getStatus().toString())
                    .findFirst()
                    .orElse("NEW"); // Mặc định là NEW nếu chưa học

            return new CardResponse(
                    card.getCardId(),
                    card.getTerm(),
                    card.getDefinition(),
                    Optional.ofNullable(card.getCardImage()),
                    Optional.of(status)
            );
        }).collect(Collectors.toList());
    }

    @Override
    public StudySetResponse createStudySet(StudySetRequest studySetRequest) {
        try {
            User currentUser = userRepository.findByEmail(studySetRequest.getUserEmail()).orElseThrow(
                    () -> new IllegalArgumentException("User not found")
            );

            validateRequest(studySetRequest);

//            String s3ImageUrl = s3Service.uploadFile(studySetRequest.getFile());
            StudySet studySet = new StudySet();
            studySet.setTitle(studySetRequest.getTitle());
            studySet.setDescription(studySetRequest.getDescription());

            if (studySetRequest.getCoverImage() != null && !studySetRequest.getCoverImage().isEmpty()) {
                String coverUrl = s3Service.uploadFile(studySetRequest.getCoverImage());
                studySet.setCoverImage(coverUrl);
            }
            studySet.setPublic(true);
            studySet.setUser(currentUser);

            List<CardRequest> cards = studySetRequest.getCards();
            studySet.setCards(cards.stream().map(cardRequest -> {
                Card card = new Card();
                card.setTerm(cardRequest.getTerm());
                card.setDefinition(cardRequest.getDefinition());
                card.setStudySet(studySet);
                if (cardRequest.getCardImage() != null && !cardRequest.getCardImage().isEmpty()) {
                    String cardImageUrl = s3Service.uploadFile(cardRequest.getCardImage());
                    card.setCardImage(cardImageUrl);
                }
                return card;
            }).collect(Collectors.toList()));
//            studySet.setCards(cards);
            StudySet savedStudySet = studySetRepository.save(studySet);

            return getStudySetResponse(savedStudySet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create study set: " + e.getMessage());
        }
    }

    @Override
    public List<StudySetResponse> getAllStudySets(String userEmail) {
        User currentUser = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new IllegalArgumentException("User not found")
        );
        return studySetRepository.findAllSavedStudySetsByUserEmail(currentUser.getUserId()).stream().map(this::getStudySetResponse).collect(Collectors.toList());

    }

    private StudySetResponse getStudySetResponse(StudySet studySet) {
        StudySetResponse response = new StudySetResponse();
        response.setStudySetId(studySet.getStudySetId());
        response.setTitle(studySet.getTitle());
        response.setDescription(studySet.getDescription());
        response.setCoverImage(studySet.getCoverImage());
        response.setPublic(studySet.isPublic());

        if (studySet.getUser() != null) {
            response.setUserName(studySet.getUser().getFirstName() + " " + studySet.getUser().getLastName());
            response.setAvatarUrl(studySet.getUser().getAvatarUrl());
        }

        // Ánh xạ danh sách Card
        List<CardResponse> cardResponses = studySet.getCards().stream()
                .map(card -> {
                    CardResponse cr = new CardResponse();
                    cr.setCardId(card.getCardId());
                    cr.setTerm(card.getTerm());
                    cr.setCardImage(Optional.ofNullable(card.getCardImage()));
                    cr.setDefinition(card.getDefinition());
                    return cr;
                }).collect(Collectors.toList());

        response.setCards(cardResponses);

        return response;
    }


    @Override
    public StudySetResponse getStudySetById(Long id) {
        if (!studySetRepository.existsById(id)) {
            throw new IllegalArgumentException("Study set not found");
        }

        return getStudySetResponse(studySetRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("Study set not found")
        ));
    }

    @Override
    @Transactional // Rất quan trọng để Hibernate quản lý phiên làm việc
    public StudySetResponse updateStudySet(Long id, StudySetRequest studySetRequest) {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Study set not found"));

        // 1. Cập nhật các thông tin cơ bản

        studySet.setTitle(studySetRequest.getTitle());
        studySet.setDescription(studySetRequest.getDescription());
        studySet.setCoverImage(String.valueOf(studySetRequest.getCoverImage()));
        studySet.setPublic(true);


        studySet.getCards().clear();

        if (studySetRequest.getCards() != null) {
            studySetRequest.getCards().forEach(cardRequest -> {
                Card card = new Card();
                card.setTerm(cardRequest.getTerm());
                card.setDefinition(cardRequest.getDefinition());
                card.setStudySet(studySet);
                studySet.getCards().add(card);
            });
        }

        // 3. Lưu lại
        StudySet updatedStudySet = studySetRepository.save(studySet);
        return getStudySetResponse(updatedStudySet);
    }

    @Override
    public void deleteStudySet(Long id) {
        if (!studySetRepository.existsById(id)) {
            throw new IllegalArgumentException("Study set not found");
        }
        studySetRepository.deleteById(id);
    }


    @Override
    public List<TestQuestionDto> generateTest(Long studySetId, int limit, TestType testType) {
        List<Card> allCards = cardRepository.findByStudySetId(studySetId);
        if (allCards.isEmpty()) return new ArrayList<>();

        Collections.shuffle(allCards);
        List<Card> testCards = allCards.stream().limit(limit).toList();
        List<TestQuestionDto> testQuestions = new ArrayList<>();
        Random random = new Random();

        for (Card card : testCards) {
            TestQuestionDto dto = new TestQuestionDto();
            dto.setCardId(card.getCardId());
            dto.setTestType(testType);
            dto.setQuestion(card.getTerm());

            switch (testType) {
                case MULTIPLE_CHOICE:
                    List<String> distractors = allCards.stream()
                            .filter(c -> !c.getCardId().equals(card.getCardId()))
                            .map(Card::getDefinition)
                            .collect(Collectors.toList());
                    Collections.shuffle(distractors);

                    List<String> options = new ArrayList<>(distractors.subList(0, Math.min(3, distractors.size())));
                    options.add(card.getDefinition());
                    Collections.shuffle(options);
                    dto.setOptions(options);
                    break;

                case TRUE_FALSE:
                    // BE quyết định hiển thị định nghĩa đúng hay sai
                    boolean shouldShowCorrect = random.nextBoolean();
                    if (shouldShowCorrect) {
                        dto.setShowDefinitions(card.getDefinition());
                    } else {
                        String fakeDef = allCards.stream()
                                .filter(c -> !c.getCardId().equals(card.getCardId()))
                                .map(Card::getDefinition)
                                .findAny().orElse(card.getDefinition());
                        dto.setShowDefinitions(fakeDef);
                    }
                    // KHÔNG trả về correctAnswer.
                    // BE sẽ tự tính toán lại dựa trên cardId khi người dùng nộp bài (True/False).
                    break;

                case FILL_IN_THE_BLANK:
                    String definition = card.getDefinition();
                    String maskedHint = maskString(definition);
                    dto.setPlaceHolder("Type the definition...");
                    dto.setShowDefinitions(maskedHint);
                    break;
            }
            testQuestions.add(dto);
        }
        return testQuestions;
    }
    private String maskString(String input) {
        if (input == null || input.length() <= 2) {
            return input; // Không đủ dài để che
        }

        StringBuilder masked = new StringBuilder();
        char[] chars = input.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            // Giữ lại ký tự đầu, cuối và dấu cách
            if (i == 0 || i == chars.length- 1 || chars[i] == ' ') {
                masked.append(chars[i]);
            } else {
                // Che các ký tự còn lại
                masked.append("_");
            }
        }
        return masked.toString();
    }
}

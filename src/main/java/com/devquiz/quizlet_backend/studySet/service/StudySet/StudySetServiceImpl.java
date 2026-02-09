package com.devquiz.quizlet_backend.studySet.service.StudySet;

import com.devquiz.quizlet_backend.card.dto.request.CardRequest;
import com.devquiz.quizlet_backend.card.dto.response.CardResponse;
import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.studySet.dto.request.StudySetRequest;
import com.devquiz.quizlet_backend.studySet.dto.response.StudySetResponse;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.repository.StudySetRepository;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudySetServiceImpl implements StudySetService {
    private final StudySetRepository studySetRepository;
    private final UserRepository    userRepository;


    private void validateRequest(StudySetRequest request) {
        if (request.getTitle() == null || request.getTitle().isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        if (request.getCards() == null || request.getCards().isEmpty())
            throw new IllegalArgumentException("Study set must have at least one card");
    }

    @Override
    public StudySetResponse createStudySet(StudySetRequest studySetRequest ) {
        try {
             User currentUser = userRepository.findByEmail(studySetRequest.getUserEmail()).orElseThrow(
                        () -> new IllegalArgumentException("User not found")
             );

            validateRequest(studySetRequest);

            StudySet studySet = new StudySet();
            studySet.setTitle(studySetRequest.getTitle());
            studySet.setDescription(studySetRequest.getDescription());
            studySet.setCoverImage(studySetRequest.getCoverImage());
            studySet.setPublic(true);
            studySet.setUser(currentUser);
            List<CardRequest> cards = studySetRequest.getCards();
            studySet.setCards(cards.stream().map(cardRequest -> {
                Card card = new Card();
                card.setTerm(cardRequest.getTerm());
                card.setDefinition(cardRequest.getDefinition());
                card.setStudySet(studySet);
                return card;
            }).collect(Collectors.toList()));

            StudySet savedStudySet = studySetRepository.save(studySet);

            return getStudySetResponse(savedStudySet);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create study set: " + e.getMessage());
        }
    }

    @Override
    public List<StudySetResponse> getAllStudySets() {
            return studySetRepository.findAll().stream().map(this::getStudySetResponse).collect(Collectors.toList());

    }

    private StudySetResponse getStudySetResponse(StudySet studySet) {
        StudySetResponse response = new StudySetResponse();
        response.setStudySetId(studySet.getStudySetId());
        response.setTitle(studySet.getTitle());
        response.setDescription(studySet.getDescription());
        response.setCoverImage(studySet.getCoverImage());
        response.setPublic(studySet.isPublic());

        // Ánh xạ thông tin User sang String/URL phẳng
        if (studySet.getUser() != null) {
            response.setUserName(studySet.getUser().getUserName());
            response.setAvatarUrl(studySet.getUser().getAvatarUrl());
        }

        // Ánh xạ danh sách Card
        List<CardResponse> cardResponses = studySet.getCards().stream()
                .map(card -> {
                    CardResponse cr = new CardResponse();
                    cr.setCardId(card.getCardId());
                    cr.setTerm(card.getTerm());
                    cr.setDefinition(card.getDefinition());
                    return cr;
                }).collect(Collectors.toList());

        response.setCards(cardResponses);

        return response;
    }


    @Override
    public StudySetResponse getStudySetById(Long id) {
        if (!studySetRepository.existsById(id)){
            throw new IllegalArgumentException("Study set not found");
        }

        return getStudySetResponse(studySetRepository.findById(id).get());
    }

    @Override
    @Transactional // Rất quan trọng để Hibernate quản lý phiên làm việc
    public StudySetResponse updateStudySet(Long id, StudySetRequest studySetRequest) {
        StudySet studySet = studySetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Study set not found"));

        // 1. Cập nhật các thông tin cơ bản

        studySet.setTitle(studySetRequest.getTitle());
        studySet.setDescription(studySetRequest.getDescription());
        studySet.setCoverImage(studySetRequest.getCoverImage());
        studySet.setPublic(true);

        // 2. Cập nhật danh sách Cards
        // Xóa sạch các card cũ trong list hiện tại (Hibernate sẽ tự hiểu để DELETE trong DB nhờ orphanRemoval)
        studySet.getCards().clear();

        // Thêm các card mới từ request vào chính list đó
        if (studySetRequest.getCards() != null) {
            studySetRequest.getCards().forEach(cardRequest -> {
                Card card = new Card();
                card.setTerm(cardRequest.getTerm());
//                card.setCardImage(cardRequest.getCardImage());
                card.setDefinition(cardRequest.getDefinition());
                card.setStudySet(studySet); // Thiết lập mối quan hệ
                studySet.getCards().add(card); // Thêm vào list đang được quản lý
            });
        }

        // 3. Lưu lại
        StudySet updatedStudySet = studySetRepository.save(studySet);
        return getStudySetResponse(updatedStudySet);
    }

    @Override
    public void deleteStudySet(Long id) {
        if (!studySetRepository.existsById(id)){
            throw new IllegalArgumentException("Study set not found");
        }
        studySetRepository.deleteById(id);
    }
}

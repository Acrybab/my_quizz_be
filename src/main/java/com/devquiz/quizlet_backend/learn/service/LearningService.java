package com.devquiz.quizlet_backend.learn.service;

import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.card.repository.CardRepository;
import com.devquiz.quizlet_backend.learn.dto.request.LearningRequest;
import com.devquiz.quizlet_backend.learn.dto.response.LearningProgressResponse;
import com.devquiz.quizlet_backend.learn.entity.LearningProcess;
import com.devquiz.quizlet_backend.learn.repository.LearningProgressRepository;
import com.devquiz.quizlet_backend.learn.types.LearningStatus;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.studySet.repository.StudySetRepository;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LearningService {
    private final LearningProgressRepository learningProgressRepository;
    private final UserRepository userRepository;
    private final StudySetRepository studySetRepository;
   private final CardRepository cardRepository;
    public LearningProgressResponse updateLearingProcess(LearningRequest learningRequest) {
        // 1. Tìm User và Card (Giữ nguyên logic của bạn)
        User user = userRepository.findById(learningRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Card card = cardRepository.findById(learningRequest.getCardId())
                .orElseThrow(() -> new IllegalArgumentException("Card not found"));

        // 2. Lưu/Cập nhật tiến độ (Giữ nguyên logic của bạn)
        LearningProcess process = learningProgressRepository.findByUserAndCard(user, card)
                .orElseGet(() -> {
                    LearningProcess newProcess = new LearningProcess();
                    newProcess.setUser(user);
                    newProcess.setCard(card);
                    return newProcess;
                });

        process.setStatus(learningRequest.getStatus());
        process.setLastReviewed(LocalDateTime.now());
        process.setCurrentIndex(learningRequest.getCurrentIndex());
        learningProgressRepository.save(process);

        // --- PHẦN MỚI: TÍNH TOÁN CHO PROGRESS BAR ---

        // 3. Lấy StudySet ID từ card
        Long studySetId = card.getStudySet().getStudySetId();

        // 4. Đếm tổng số thẻ trong bộ
        long totalCards = cardRepository.countByStudySet_StudySetId(studySetId);

        // 5. Đếm số thẻ User đã "MASTERED" trong bộ này
        long masteredCount = learningProgressRepository.countByUser_UserIdAndCard_StudySet_StudySetIdAndStatus(
                user.getUserId(), studySetId, com.devquiz.quizlet_backend.learn.types.LearningStatus.MASTERED);
        int currentIndex = learningRequest.getCurrentIndex();
        // 6. Tính %
        double percentage = (totalCards > 0) ? ((double) masteredCount / totalCards) * 100 : 0;
        String cardStatus = learningRequest.getStatus().name(); // Lấy trạng thái hiện tại của thẻ
        return new LearningProgressResponse(totalCards, masteredCount, percentage , currentIndex, cardStatus);
    }

    public void resetLearningProcees (String userEmail , Long studySetId){
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new IllegalArgumentException("User not found"));
        StudySet studySet = studySetRepository.findById(studySetId).orElseThrow(() -> new IllegalArgumentException("StudySet not found"));
        List<LearningProcess> process = learningProgressRepository.findAllByUserId(user.getEmail()).stream().filter(learningProcess ->
                learningProcess.getStatus() == LearningStatus.MASTERED && studySet.getStudySetId() == studySetId).peek(lpM->
        {
            lpM.setStatus(LearningStatus.NEW);

        }).toList();
        learningProgressRepository.saveAll(process);
    }

}

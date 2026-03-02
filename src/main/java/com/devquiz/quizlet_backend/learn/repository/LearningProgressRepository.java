package com.devquiz.quizlet_backend.learn.repository;

import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.learn.entity.LearningProcess;
import com.devquiz.quizlet_backend.learn.types.LearningStatus;
import com.devquiz.quizlet_backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LearningProgressRepository extends JpaRepository<LearningProcess, Long> {
    Optional<LearningProcess> findByUserAndCard(User user, Card card);


    //
    @Query("SELECT lp FROM LearningProcess lp WHERE lp.user.email = :email ")
    List<LearningProcess> findAllByUserId(String email);


    @Query("SELECT COUNT(lp) FROM LearningProcess lp WHERE lp.user.userId = :userId AND lp.card.studySet.studySetId = :studySetId AND lp.status = :status")
    long countByUser_UserIdAndCard_StudySet_StudySetIdAndStatus(Long userId, Long studySetId, LearningStatus status);

    @Query("SELECT lp.card.cardId FROM LearningProcess lp WHERE lp.user.userId = :userId AND lp.card.studySet.studySetId = :studySetId AND lp.status = :status")
    List<Long> findCardIdsByStatus(Long userId, Long studySetId, LearningStatus status);
}

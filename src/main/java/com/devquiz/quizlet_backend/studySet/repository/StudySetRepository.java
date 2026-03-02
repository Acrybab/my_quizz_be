package com.devquiz.quizlet_backend.studySet.repository;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudySetRepository extends JpaRepository<StudySet, Long> {
    @Query("SELECT DISTINCT s FROM StudySet s JOIN Card  c ON s.studySetId = c.studySet.studySetId JOIN LearningProcess lp ON c.cardId = lp.card.cardId WHERE lp.status = 'MASTERED' AND lp.user.email = :userEmail")
    List<StudySet> findAllMasteredStudySetsByUserEmail( @Param("userEmail") String userEmail);
}

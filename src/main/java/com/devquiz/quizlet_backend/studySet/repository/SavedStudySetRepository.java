package com.devquiz.quizlet_backend.studySet.repository;

import com.devquiz.quizlet_backend.studySet.entity.SavedStudySet;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedStudySetRepository extends JpaRepository<SavedStudySet, Long> {


    Optional<SavedStudySet> findByUserAndStudySet(User user, StudySet studySet);
    @Query("SELECT s FROM SavedStudySet s JOIN FETCH s.studySet WHERE s.user.userId = :userId")
    List<SavedStudySet> findAllByUserId(@Param("userId") Long userId);
}

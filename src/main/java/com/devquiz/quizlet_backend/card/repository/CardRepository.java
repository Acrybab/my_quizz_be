package com.devquiz.quizlet_backend.card.repository;

import com.devquiz.quizlet_backend.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

@Query("SELECT c FROM Card c WHERE c.studySet.studySetId = :studySetId")
List<Card> findByStudySetId(@Param("studySetId") Long studySetId);


   @Query("SELECT COUNT(c) FROM Card c WHERE c.studySet.studySetId = :studySetId")
    long countByStudySet_StudySetId(Long studySetId);



}


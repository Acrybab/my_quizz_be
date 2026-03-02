package com.devquiz.quizlet_backend.leaderboard.repository;

import com.devquiz.quizlet_backend.leaderboard.entities.LeaderBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeaderBoardRepository extends JpaRepository<LeaderBoard, Long> {

    @Query("SELECT lb From LeaderBoard  lb WHERE lb.user.userId =:userId AND lb.studySet.studySetId = :studySetId")
    Optional<LeaderBoard> findByUserIdAndStudySetId(Long userId, Long studySetId);



}

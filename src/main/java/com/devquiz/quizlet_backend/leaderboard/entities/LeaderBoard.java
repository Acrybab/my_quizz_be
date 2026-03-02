package com.devquiz.quizlet_backend.leaderboard.entities;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "leader_board")
public class LeaderBoard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leader_board_id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="study_set_id", nullable = false)
    private StudySet studySet;
    @Column(nullable = false)
    private Double bestTime;
    @Column(nullable = false)
    private Integer bestScore;
    @Column(nullable = false)
    private Integer totalAttempts = 0;
    @Column(nullable = false)
    private LocalDateTime lastPlayedAt;



}

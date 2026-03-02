package com.devquiz.quizlet_backend.learn.entity;

import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.learn.types.LearningStatus;
import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "learning_processes")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long learningProcessId;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference
    private User user;

    @Column
    private Integer currentIndex;

    @ManyToOne
    @JoinColumn(name = "card_id", nullable = false)
    @JsonBackReference
    private Card card;

    @Enumerated(EnumType.STRING)
    private LearningStatus status;

    private LocalDateTime lastReviewed;


}

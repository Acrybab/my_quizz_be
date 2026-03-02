package com.devquiz.quizlet_backend.studySet.entity;

import com.devquiz.quizlet_backend.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="saved_study_set", uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "study_set_id"})})
public class SavedStudySet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long savedStudySetId;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "study_set_id")
    private StudySet studySet;

    private LocalDateTime savedStudySetAt = LocalDateTime.now();
}

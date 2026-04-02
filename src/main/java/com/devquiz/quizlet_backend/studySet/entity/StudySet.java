package com.devquiz.quizlet_backend.studySet.entity;

import com.devquiz.quizlet_backend.card.entity.Card;
import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.patterns.TypePatternQuestions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="study_sets")
public class StudySet {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY )
    private long studySetId;
    @Column(unique=false , nullable = false)
    private String title;
    @Column( nullable = true,columnDefinition = "TEXT")
    private String description;

    @Column( nullable = true )
    private boolean isPublic;
    @Column(nullable = true )
    private String coverImage;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore // Ngăn vòng lặp vô hạn khi serialize
    private User user;
    @OneToMany (mappedBy = "studySet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // Cho phép render danh sách Cards từ StudySet
    private List<Card> cards  = new ArrayList<>();
    @Column(name = "created_study_set_at")
    private LocalDateTime createdStudySetAt;
    @Column(name = "updated_study_set_at")
    private LocalDateTime updatedStudySetAt;
    @PrePersist
    protected void onCreate() {
        createdStudySetAt = LocalDateTime.now();
        updatedStudySetAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        updatedStudySetAt = LocalDateTime.now();
    }

}

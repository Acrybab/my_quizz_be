package com.devquiz.quizlet_backend.card.entity;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;
  @Column(nullable = false)
    private String term;
    @Column(nullable = false)
    private String definition;

    @Column(nullable = true)
    private String cardImage;
    @ManyToOne
    @JoinColumn(name = "study_set_id")
    @JsonBackReference // Ngăn không cho Jackson render ngược lại StudySet từ Card
    private StudySet studySet;


}

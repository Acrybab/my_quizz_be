package com.devquiz.quizlet_backend.card.entity;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
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
    @ManyToOne
    @JoinColumn(name = "study_set_id")
    private StudySet studySet;


}

package com.devquiz.quizlet_backend.group.entity;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "study_groups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudyGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long groupId;

    @Column(nullable = false)
    private String studyGroupName;

    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User admin;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @JsonIgnore // Quan trọng: Tránh vòng lặp vô hạn khi render JSON
    private List<GroupMember> members;

    @ManyToMany
    @JoinTable(
            name = "group_study_sets",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "study_set_id")
    )
    private List<StudySet> studySets;
}

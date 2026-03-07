package com.devquiz.quizlet_backend.group.entity;

import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "group_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long group_member_id;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
//    @JsonBackReference
    private StudyGroup group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String role; // Ví dụ: "MEMBER", "MODERATOR"

}

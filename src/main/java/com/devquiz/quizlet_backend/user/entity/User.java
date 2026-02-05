package com.devquiz.quizlet_backend.user.entity;

import com.devquiz.quizlet_backend.user.type.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = false)
    private String userName;
    @Column(nullable = false)
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true , nullable = true)
    private String firstName;
    @Column(unique = true , nullable = true)
    private String lastName;
    @Enumerated(EnumType.STRING)
    @Builder.Default // Quan trọng: Giúp Builder nhận giá trị mặc định nếu bạn không set
    private Role.RoleType role = Role.RoleType.USER; // Mặc định là USER khi đăng ký
    @Column(nullable = true)
    private String avatarUrl;
    @Column(nullable = false)
    private Boolean isEnabled;
    @Column(name = "created_at")
    private LocalDateTime createdUserAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedUserAt;
    @PrePersist
    protected void onCreate() {
        createdUserAt = LocalDateTime.now();
        updatedUserAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedUserAt = LocalDateTime.now();
    }

}

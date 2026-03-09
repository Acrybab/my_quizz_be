package com.devquiz.quizlet_backend.user.entity;

import com.devquiz.quizlet_backend.studySet.entity.StudySet;
import com.devquiz.quizlet_backend.user.type.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @Column(unique = true, nullable = true)
    private String userName;
    @Column(nullable = false)
    @JsonIgnore
    private String password;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique =false , nullable = true)
    private String firstName;
    @Column(unique = false , nullable = true)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudySet> studySets = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdUserAt = LocalDateTime.now();
        updatedUserAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedUserAt = LocalDateTime.now();
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về danh sách quyền (ví dụ: ROLE_USER)
        return List.of();
    }

    @Override
    public String getUsername() {
        return this.email; // Hoặc field nào bạn dùng để đăng nhập
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

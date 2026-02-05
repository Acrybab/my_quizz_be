package com.devquiz.quizlet_backend.user.dto.response;

import com.devquiz.quizlet_backend.user.type.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
 private Long userId;
 private String userName;
 private String email;
 private String firstName;
 private String lastName;
 private Role.RoleType role;
 private LocalDateTime createdUserAt;
}

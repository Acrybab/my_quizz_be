package com.devquiz.quizlet_backend.user.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyRequest {
    private String email;
    private String otp;
}

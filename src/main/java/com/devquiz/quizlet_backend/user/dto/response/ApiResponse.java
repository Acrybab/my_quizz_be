package com.devquiz.quizlet_backend.user.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ hiện các trường không null
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;
}

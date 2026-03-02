package com.devquiz.quizlet_backend.learn.dto.response;

public record LearningProgressResponse(
        long totalCards,
        long masteredCount,
        double percentage,
        int currentIndex,
        String cardStatus
) {}
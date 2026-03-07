package com.devquiz.quizlet_backend.notification.service;

import com.devquiz.quizlet_backend.notification.entities.Notification;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface NotificationService {
//    void sendNotificationToUser(Long userId, String message);
void createAndSendNotification(User recipient, String title, String message, String targetUrl);
    List<Notification> getNotificationsForUser(String userEmail);

}

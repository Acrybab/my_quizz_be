package com.devquiz.quizlet_backend.notification.service.impl;

import com.devquiz.quizlet_backend.notification.dto.NotificationType;
import com.devquiz.quizlet_backend.notification.entities.Notification;


import com.devquiz.quizlet_backend.notification.repository.NotificationRepository;
import com.devquiz.quizlet_backend.notification.service.NotificationService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;
import com.devquiz.quizlet_backend.user.entity.User;
import com.devquiz.quizlet_backend.user.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final SimpMessagingTemplate messagingTemplate;
     private  final NotificationRepository notificationRepository;
     private final UserRepository userRepository;
    @Override
    public void createAndSendNotification(User recipient, String title, String message, String targetUrl) {
        System.out.println("Creating notification for user: " + recipient.getUsername() + " with title: " + title);
        Notification notification = new Notification();
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRecipient(recipient);
        notification.setTargetUrl(targetUrl);
        notification.setRead(false);
        notification.setType(NotificationType.GROUP_INVITE);
        notification.setCreatedAt(LocalDateTime.now());
       notificationRepository.save(notification);
        messagingTemplate.convertAndSendToUser(recipient.getEmail(), "/queue/notifications", notification);

    }

    @Override
    public List<Notification>getNotificationsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(
                () -> new RuntimeException("User not found with id: " + userEmail)
        );
        List<Notification> notifications = notificationRepository.findTop20ByRecipient_UserIdOrderByCreatedAtDesc(user.getUserId());

        return notifications;


    }
}

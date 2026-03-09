package com.devquiz.quizlet_backend.notification.controller;

import com.devquiz.quizlet_backend.notification.entities.Notification;
import com.devquiz.quizlet_backend.notification.service.NotificationService;
import com.devquiz.quizlet_backend.user.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/get-my-notifications")
    public ResponseEntity<ApiResponse<List<Notification>>> getMyNotifications(Principal principal) {
        System.out.println("Fetching notifications for user: " + principal.getName());
        List<Notification> notifications = notificationService.getNotificationsForUser(principal.getName());
        return ResponseEntity.ok(new ApiResponse<>(200, "Success", notifications));
    }
}

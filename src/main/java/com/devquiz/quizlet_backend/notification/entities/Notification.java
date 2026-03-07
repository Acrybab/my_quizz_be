package com.devquiz.quizlet_backend.notification.entities;

import com.devquiz.quizlet_backend.notification.dto.NotificationType;
import com.devquiz.quizlet_backend.user.entity.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String title; // Tiêu đề ví dụ: "Lời mời tham gia nhóm"

    private String message; // Nội dung ví dụ: "Admin Khang đã thêm bạn vào nhóm Java Spring Boot"

    @Enumerated(EnumType.STRING)
    private NotificationType type; // GROUP_INVITE, NEW_QUIZ, SYSTEM_UPDATE

    private String targetUrl; // Link để khi user click vào thông báo sẽ dẫn đến trang đó (ví dụ: /groups/1)

    private boolean isRead = false; // Trạng thái đã đọc hay chưa

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore // BẮT BUỘC phải có dòng này
    @JoinColumn(name = "user_id")
    private User recipient; // Người nhận thông báo

    private LocalDateTime createdAt;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

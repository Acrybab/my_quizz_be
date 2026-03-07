package com.devquiz.quizlet_backend.notification.repository;

import com.devquiz.quizlet_backend.notification.entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByRecipient_UserIdOrderByCreatedAtDesc(Long userId);

    // Nếu bạn muốn giới hạn chỉ lấy 20 thông báo gần nhất để load cho nhanh (giống Facebook)
    List<Notification> findTop20ByRecipient_UserIdOrderByCreatedAtDesc(Long userId);
}

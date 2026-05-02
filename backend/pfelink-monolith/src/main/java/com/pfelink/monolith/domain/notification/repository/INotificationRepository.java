package com.pfelink.monolith.domain.notification.repository;

import com.pfelink.monolith.domain.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface INotificationRepository {
    Notification save(Notification notification);
    Optional<Notification> findById(UUID id);
    Page<Notification> findByUserId(String userId, Pageable pageable);
    long countByUserIdAndReadFalse(String userId);
    void markAsRead(UUID notificationId, String userId);
    void markAllAsRead(String userId);
    void deleteByIdAndUserId(UUID notificationId, String userId);
}

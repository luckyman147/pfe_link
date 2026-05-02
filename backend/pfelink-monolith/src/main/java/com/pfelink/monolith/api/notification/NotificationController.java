package com.pfelink.monolith.api.notification;

import com.pfelink.monolith.domain.notification.entity.Notification;
import com.pfelink.monolith.domain.notification.repository.INotificationRepository;
import com.pfelink.monolith.infrastructure.api.ResponseUtil;
import com.pfelink.monolith.infrastructure.security.util.SecurityUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(name = "Notifications", description = "In-app notification management")
public class NotificationController {

    private final INotificationRepository notificationRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getMyNotifications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int limit) {
        String userId = SecurityUtils.getCurrentUserId().toString();
        Page<Notification> notifications = notificationRepository.findByUserId(userId, PageRequest.of(page, limit));
        return ResponseUtil.toResponse(notifications);
    }

    @GetMapping("/me/unread-count")
    public ResponseEntity<?> getUnreadCount() {
        String userId = SecurityUtils.getCurrentUserId().toString();
        long count = notificationRepository.countByUserIdAndReadFalse(userId);
        return ResponseUtil.toResponse(java.util.Map.of("count", count));
    }

    @PatchMapping("/me/read/{notificationId}")
    public ResponseEntity<?> markAsRead(@PathVariable java.util.UUID notificationId) {
        String userId = SecurityUtils.getCurrentUserId().toString();
        notificationRepository.markAsRead(notificationId, userId);
        return ResponseUtil.toResponse(java.util.Map.of("message", "Marked as read"));
    }

    @PatchMapping("/me/read-all")
    public ResponseEntity<?> markAllAsRead() {
        String userId = SecurityUtils.getCurrentUserId().toString();
        notificationRepository.markAllAsRead(userId);
        return ResponseUtil.toResponse(java.util.Map.of("message", "All marked as read"));
    }
}

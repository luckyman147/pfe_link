package com.pfelink.monolith.infrastructure.persistence.notification;

import com.pfelink.monolith.domain.notification.entity.Notification;
import com.pfelink.monolith.domain.notification.repository.INotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaNotificationRepository implements INotificationRepository {

    private final SpringDataNotificationRepository springRepo;

    @Override public Notification save(Notification n) { return springRepo.save(n); }
    @Override public Optional<Notification> findById(UUID id) { return springRepo.findById(id); }
    @Override public Page<Notification> findByUserId(String uid, Pageable p) { return springRepo.findByUserIdOrderByCreatedAtDesc(uid, p); }
    @Override public long countByUserIdAndReadFalse(String uid) { return springRepo.countByUserIdAndReadFalse(uid); }
    @Override public void markAsRead(UUID id, String uid) { springRepo.markAsRead(id, uid); }
    @Override public void markAllAsRead(String uid) { springRepo.markAllAsRead(uid); }
    @Override public void deleteByIdAndUserId(UUID id, String uid) { springRepo.deleteByIdAndUserId(id, uid); }
}

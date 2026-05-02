package com.pfelink.monolith.infrastructure.event.listeners.selection;

import com.pfelink.monolith.domain.academic.entity.selection.SelectionRequest;
import com.pfelink.monolith.domain.notification.entity.Notification;
import com.pfelink.monolith.domain.notification.enums.NotificationType;
import com.pfelink.monolith.domain.notification.repository.INotificationRepository;
import com.pfelink.monolith.infrastructure.email.EmailService;
import com.pfelink.monolith.infrastructure.event.events.selection.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SelectionNotificationListener {

    private final EmailService emailService;
    private final INotificationRepository notificationRepository;

    @Async
    @EventListener
    public void onSelectionRequestSubmitted(SelectionRequestSubmittedEvent event) {
        log.info("Selection request submitted to advisor: {}", event.request().getAdvisor().getEmail());
        
        String ownerName = event.request().getProject().getOwner().getFullName();
        String projectTitle = event.request().getProject().getTitle();

        EmailService.SelectionEmailData emailData = new EmailService.SelectionEmailData(
            event.request().getId().toString(),
            ownerName,
            event.request().getProject().getOwner().getEmail(),
            event.request().getAdvisor().getFullName(),
            event.request().getFaculty().getName(),
            projectTitle,
            event.request().getMessage()
        );

        emailService.sendSelectionRequestToAdvisorEmail(event.request().getAdvisor().getEmail(), emailData);

        Notification notification = new Notification();
        notification.setUserId(event.request().getAdvisor().getUserId().toString());
        notification.setType(NotificationType.IN_APP);
        notification.setTitle("New Project Supervision Request");
        notification.setMessage("Project '" + projectTitle + "' (Owner: " + ownerName 
            + ") has requested you as their advisor.");
        notificationRepository.save(notification);
    }

    @Async
    @EventListener
    public void onSelectionRequestResponded(SelectionRequestRespondedEvent event) {
        SelectionRequest request = event.request();
        log.info("Selection request responded for project: {}", request.getProject().getTitle());
        
        request.getProject().getMembers().forEach(member -> {
            emailService.sendSelectionResponseToStudentEmail(
                member.getEmail(), member.getFullName(),
                request.getAdvisor().getFullName(), request.getStatus().name());

            Notification notification = new Notification();
            notification.setUserId(member.getUserId().toString());
            notification.setType(NotificationType.IN_APP);
            notification.setTitle("Selection Request " + request.getStatus());
            notification.setMessage("Your project '" + request.getProject().getTitle() 
                + "' request for advisor " + request.getAdvisor().getFullName() 
                + " has been " + request.getStatus().name().toLowerCase());
            notificationRepository.save(notification);
        });
    }
}

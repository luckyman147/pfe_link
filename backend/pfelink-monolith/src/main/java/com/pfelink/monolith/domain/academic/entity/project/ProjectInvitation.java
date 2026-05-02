package com.pfelink.monolith.domain.academic.entity.project;

import com.pfelink.monolith.domain.academic.entity.profile.StudentProfile;
import com.pfelink.monolith.domain.academic.enums.ProjectInvitationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "project_invitations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInvitation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invitee_id", nullable = false)
    private StudentProfile invitee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectInvitationStatus status = ProjectInvitationStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime respondedAt;
}

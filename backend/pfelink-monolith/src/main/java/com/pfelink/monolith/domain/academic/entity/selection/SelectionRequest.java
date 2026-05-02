package com.pfelink.monolith.domain.academic.entity.selection;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.SelectionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "selection_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advisor_profile_id", nullable = false)
    private AdvisorProfile advisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SelectionStatus status = SelectionStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime respondedAt;
}

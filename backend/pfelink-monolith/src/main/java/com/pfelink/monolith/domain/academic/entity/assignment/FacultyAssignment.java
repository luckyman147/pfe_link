package com.pfelink.monolith.domain.academic.entity.assignment;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.entity.profile.AdvisorProfile;
import com.pfelink.monolith.domain.academic.entity.season.Season;
import com.pfelink.monolith.domain.academic.enums.AdvisorRole;
import com.pfelink.monolith.domain.academic.enums.AssignmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "faculty_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyAssignment {

    @EmbeddedId
    private FacultyAssignmentId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("advisorId")
    @JoinColumn(name = "advisor_id")
    private AdvisorProfile advisor;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("facultyId")
    @JoinColumn(name = "faculty_id")
    private Faculty faculty;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("seasonId")
    @JoinColumn(name = "season_id")
    private Season season;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AdvisorRole advisorRole;

    @Column(nullable = false)
    private Integer maxCapacity;

    @Column(nullable = false)
    private Integer currentStudentCount = 0;

    @Column(nullable = false)
    private String facultyDomainEmail;

    @Column(nullable = false)
    private String professionalProofUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssignmentStatus status = AssignmentStatus.PENDING;
}

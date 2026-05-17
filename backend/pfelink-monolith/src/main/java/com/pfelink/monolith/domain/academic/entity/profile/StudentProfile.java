package com.pfelink.monolith.domain.academic.entity.profile;

import com.pfelink.monolith.domain.academic.entity.faculty.Faculty;
import com.pfelink.monolith.domain.academic.entity.project.Project;
import com.pfelink.monolith.domain.academic.enums.student.StudentStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "email")
    private String email;

    private String studentCardUrl;

    @Column(name = "cin_number", nullable = false, unique = true)
    private String cinNumber;

    @Column(name = "faculty_id")
    private UUID facultyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", insertable = false, updatable = false)
    private Faculty faculty;

    @Column(name = "faculty_approved", nullable = false)
    private boolean facultyApproved = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StudentStatus status = StudentStatus.PENDING;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    public StudentProfile(UUID userId, String fullName, String email, String cinNumber, String studentCardUrl, Faculty faculty) {
        this.userId = userId;
        this.fullName = fullName;
        this.email = email;
        this.cinNumber = cinNumber;
        this.studentCardUrl = studentCardUrl;
        this.faculty = faculty;
        this.facultyId = faculty != null ? faculty.getId() : null;
    }
}

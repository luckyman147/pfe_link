package com.pfelink.monolith.domain.academic.entity.profile;

import com.pfelink.monolith.domain.academic.entity.assignment.AdvisorAssignment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "advisor_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdvisorProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "email")
    private String email;

    @Column(nullable = false)
    private String fullName;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "cin_number", nullable = false, unique = true)
    private String cinNumber;

    @Column(name = "cin_screenshot_url")
    private String cinScreenshotUrl;

    @Column(name = "pending_faculty_id")
    private UUID pendingFacultyId;

    @Column(name = "pending_faculty_domain_email")
    private String pendingFacultyDomainEmail;

    @OneToMany(mappedBy = "advisor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AdvisorAssignment> assignments = new ArrayList<>();
}

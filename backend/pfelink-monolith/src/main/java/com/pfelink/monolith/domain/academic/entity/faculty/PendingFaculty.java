package com.pfelink.monolith.domain.academic.entity.faculty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "pending_faculties")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingFaculty {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    private String abbreviation;
    private String email;
    private String websiteUrl;
    private String imageUrl;
    private String adminPassword;

    // Detailed Address Fields for OSM support
    private String path;
    private Double latitude;
    private Double longitude;
    private String country;
    private String countryCode;
    private String region;
    private String governorate;
    private String city;
    private String suburb;
    private String quarter;
    private String neighborhood;
    private String street;
    private String houseNumber;
    private String postalCode;
    
    @Column(columnDefinition = "text")
    private String displayName;
}

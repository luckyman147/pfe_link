package com.pfelink.monolith.domain.academic.entity.faculty;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // PostgreSql ltree path (e.g., "Tunisia.Tunis.Carthage")
    @Column(columnDefinition = "ltree")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.VARCHAR)
    @org.hibernate.annotations.ColumnTransformer(write = "?::ltree", read = "path::text")
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

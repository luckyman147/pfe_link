package com.pfelink.monolith.domain.academic.entity.assignment;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacultyAssignmentId implements Serializable {
    private UUID advisorId;
    private UUID facultyId;
    private UUID seasonId;
}

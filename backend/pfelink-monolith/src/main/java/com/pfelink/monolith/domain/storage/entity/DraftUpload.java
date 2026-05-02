package com.pfelink.monolith.domain.storage.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "draft_uploads")
@Getter
@Setter
public class DraftUpload {

    @Id
    private String id; // UUID as String

    private String url;

    private String publicId;

    private LocalDateTime createdAt = LocalDateTime.now();

    private boolean committed = false;
}

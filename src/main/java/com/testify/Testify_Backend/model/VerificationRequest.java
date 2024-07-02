package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "verification_request")
public class VerificationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private long organizationId;
    private String verificationDocumentUrl;
    private String verificationStatus;
    private String rejectionReason;
    private Date requestDate;

    @PrePersist // This annotation is used to execute the method before the entity is persisted(saved) to the database.
    protected void onCreate() {
        requestDate = new Date();
    }
}

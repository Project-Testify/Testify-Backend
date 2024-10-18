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

//   private long organizationId;  add a foreing key to the organization table
    @ManyToOne(fetch = FetchType.LAZY) // Lazy fetch as you may not always need organization details when loading a verification request
    @JoinColumn(name = "organization_id", nullable = false)// Lazy fetch as you may not always need organization details when loading a verification request
    private Organization organization;

    private String verificationDocument01Url;
    private String verificationDocument02Url;
    private String verificationDocument03Url;
    private String verificationDocument04Url;
    private String verificationDocument05Url;


    private String verificationStatus;
    private String rejectionReason;
    private Date requestDate;



    @PrePersist // This annotation is used to execute the method before the entity is persisted(saved) to the database.
    protected void onCreate() {
        requestDate = new Date();
    }
}

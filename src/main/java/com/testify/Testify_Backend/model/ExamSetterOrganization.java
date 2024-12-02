package com.testify.Testify_Backend.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;
import org.springframework.data.annotation.Id;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExamSetterOrganization {
    @Setter
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    @Column
    private Long id;

    @Column(nullable = false)
    private String organizationID;

    @Column(nullable = false)
    private String examSetterID;

    private boolean isDeleted = false;

    public Long getId() {
        return id;
    }

}

package com.testify.Testify_Backend.model;

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
public class ExamCandidateGrade {

    @Setter
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-generate the ID
    @Column
    private Long id;

    @Column(nullable = false)
    private String examID;

    @Column(nullable = false)
    private String candidateID;

    private String status;

    private String grade;

    private String score;

    public Long getId() {
        return id;
    }
}

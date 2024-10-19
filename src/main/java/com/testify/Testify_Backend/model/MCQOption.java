package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@NoArgsConstructor
public class MCQOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String optionText;

    @ManyToOne
    @JoinColumn(name = "mcq_question_id", nullable = false)
    private MCQ mcqQuestion;

    private boolean correct;

    @Column(nullable = false)
    private double marks = 0;
}

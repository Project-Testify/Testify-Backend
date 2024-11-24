package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("MCQ")
public class MCQ extends Question {
    @OneToMany(mappedBy = "mcqQuestion", cascade = CascadeType.ALL , orphanRemoval = true)
    private Set<MCQOption> options;
}


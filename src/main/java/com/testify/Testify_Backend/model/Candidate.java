package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Set;

@Entity
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidate")
@PrimaryKeyJoinColumn(name = "id")
public class Candidate extends User{
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private String bio;

    @ManyToMany(mappedBy = "candidates")
    private Set<Exam> exams;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProctorComment> comments;

}

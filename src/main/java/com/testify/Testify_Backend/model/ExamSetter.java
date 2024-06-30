package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "examSetter")
@PrimaryKeyJoinColumn(name = "id")
public class ExamSetter extends User{
    @Column(nullable = false)
    private String firstName;
    private String lastName;
    private String bio;

    @ManyToMany(mappedBy = "proctors")
    private Set<Exam> proctoredExams;

    @OneToMany(mappedBy = "moderator")
    private Set<Exam> moderatedExams;
}

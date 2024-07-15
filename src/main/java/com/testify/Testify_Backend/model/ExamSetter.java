package com.testify.Testify_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
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

    @ManyToMany(mappedBy = "examSetters", cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Organization> organizations;

    @ManyToMany
    @JoinTable(
            name = "course_module_exam_setter",
            joinColumns = @JoinColumn(name = "exam_setter_id"),
            inverseJoinColumns = @JoinColumn(name = "course_module_id")
    )
    private Set<CourseModule> courseModules;

    @ManyToMany(mappedBy = "proctors")
    private Set<Exam> proctoredExams;

    @OneToMany(mappedBy = "moderator")
    private Set<Exam> moderatedExams;
}

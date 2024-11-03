package com.testify.Testify_Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization")
@PrimaryKeyJoinColumn(name = "id")
@ToString(callSuper = true)
public class Organization extends User{
    @Column(nullable = false)
    private String firstName;
    private String addressLine1;
    private String addressLine2;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String state;
    private String website;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "organization_exam_setter",
            joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "exam_setter_id"))
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ExamSetter> examSetters;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<CourseModule> courseModules;
}

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
}

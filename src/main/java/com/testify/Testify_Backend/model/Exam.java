package com.testify.Testify_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;
    private String description;
    private String instructions;
    private int duration;
    private int totalMarks;
    private int passMarks;

    @ManyToOne
    private User creator;
    @ManyToOne
    private Organization organization;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @OneToMany(mappedBy = "enrolled_exam", cascade = CascadeType.ALL)
    private Set<User> users;

}

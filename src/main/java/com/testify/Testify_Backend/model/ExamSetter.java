package com.testify.Testify_Backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

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
}

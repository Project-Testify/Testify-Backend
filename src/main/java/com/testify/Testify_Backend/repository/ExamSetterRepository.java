package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamSetter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamSetterRepository extends JpaRepository<ExamSetter, Long> {
    Optional<ExamSetter> findByEmail(String email);
}

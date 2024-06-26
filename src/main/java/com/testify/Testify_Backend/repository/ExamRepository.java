package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Exam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExamRepository extends JpaRepository<Exam, Long> {
}

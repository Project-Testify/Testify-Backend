package com.testify.Testify_Backend.repository;


import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Set<Exam> findByOrganizationId(Long id);
}

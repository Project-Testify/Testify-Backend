package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamCandidateGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamCandidateGradeRepository extends JpaRepository<ExamCandidateGrade, String> {
}
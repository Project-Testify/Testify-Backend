package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamCandidateGrade;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamCandidateGradeRepository extends JpaRepository<ExamCandidateGrade, String> {
    List<ExamCandidateGrade> findByCandidateID(String candidateId);
}
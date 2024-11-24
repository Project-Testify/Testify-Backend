package com.testify.Testify_Backend.repository;


import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Set<Exam> findByOrganizationId(Long id);

    // Fetch exams associated with a particular candidate
    @Query("SELECT e FROM Exam e JOIN e.candidates c WHERE c.id = :candidateId")
    List<Exam> findExamsByCandidateId(@Param("candidateId") Long candidateId);

    // Fetch all public (non-private) exams
    @Query("SELECT e FROM Exam e WHERE e.isPrivate = false")
    List<Exam> findAllPublicExams();
}

package com.testify.Testify_Backend.repository;


import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

public interface ExamRepository extends JpaRepository<Exam, Long> {
    Set<Exam> findByOrganizationId(Long id);
    Optional<Exam> findById(Long id);

    @Query("SELECT e FROM Exam e WHERE e.organization.id = :organizationId " +
            "AND e.id <> :examId " +
            "AND ((e.startDatetime BETWEEN :startDatetime AND :endDatetime) " +
            "OR (e.endDatetime BETWEEN :startDatetime AND :endDatetime))")
    List<Exam> findExamsScheduledBetween(long organizationId, long examId, LocalDateTime startDatetime, LocalDateTime endDatetime);

    // Fetch exams associated with a particular candidate
    @Query("SELECT e FROM Exam e JOIN e.candidates c WHERE c.id = :candidateId")
    List<Exam> findExamsByCandidateId(@Param("candidateId") Long candidateId);

    // Fetch all public (non-private) exams
    @Query("SELECT e FROM Exam e WHERE e.isPrivate = false")
    List<Exam> findAllPublicExams();

    @Query("SELECT e FROM Exam e JOIN e.proctors p WHERE p.id = :proctorId AND e.organization.id = :organizationId")
    Set<Exam> findByProctorIdAndOrganizationId(@Param("proctorId") Long proctorId, @Param("organizationId") Long organizationId);


}


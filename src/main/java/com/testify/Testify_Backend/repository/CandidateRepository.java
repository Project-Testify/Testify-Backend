package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String currentUserEmail);
    Set<Candidate> findAllByEmailIn(List<String> emails);

    @Query("SELECT c FROM Candidate c JOIN c.exams e " +
            "WHERE e.id = :examId " +
            "AND (e.startDatetime BETWEEN :startDatetime AND :endDatetime OR " +
            "e.endDatetime BETWEEN :startDatetime AND :endDatetime)")
    List<Candidate> findCandidatesAssignedToExamWithConflictingExams(Long examId, LocalDateTime startDatetime, LocalDateTime endDatetime);

    boolean existsByEmail(String currentUserEmail);

    @Query("SELECT c FROM Candidate c JOIN c.exams e WHERE e.id = :examId")
    Set<Candidate> findByExamId(@Param("examId") Long examId);
}


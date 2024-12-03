package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.CandidateExamSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamSessionRepository extends JpaRepository<CandidateExamSession, Long> {
    Optional<CandidateExamSession> findByCandidateIdAndExamIdAndInProgress(Long candidateId, Long examId, Boolean inProgress);
    Optional<CandidateExamSession> findByExamIdAndCandidateId(Long examId, Long candidateId);

    @Query("SELECT s FROM CandidateExamSession s WHERE s.candidate.id = :candidateId AND s.inProgress = true AND s.endTime >= CURRENT_TIMESTAMP")
    CandidateExamSession findInProgressSession(Long candidateId);
}


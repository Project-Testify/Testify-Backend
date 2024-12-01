package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.CandidateExamAnswer;
import com.testify.Testify_Backend.model.CandidateExamSession;
import com.testify.Testify_Backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CandidateExamAnswerRepository extends JpaRepository<CandidateExamAnswer, Long> {

    Optional<CandidateExamAnswer> findByCandidateExamSessionIdAndQuestionId (Long sessionId, Long questionId);
    @Query("SELECT e.answerText FROM EssayAnswer e WHERE e.id = :id")
    String findEssayAnswerTextById(@Param("id") Long id);

    CandidateExamAnswer findByCandidateExamSessionIdAndQuestionId(Long candidateExamSession_id, long question_id);
}

package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.CandidateExamAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidateExamAnswerRepository extends JpaRepository<CandidateExamAnswer, Long> {

}

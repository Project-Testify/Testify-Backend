package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.MCQ;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MCQQuestionRepository extends JpaRepository<MCQ, Long> {

}

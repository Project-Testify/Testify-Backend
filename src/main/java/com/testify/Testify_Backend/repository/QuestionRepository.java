package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long>{
}

package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.MCQ;
import com.testify.Testify_Backend.model.MCQOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MCQOptionRepository extends JpaRepository<MCQOption, Long> {
}

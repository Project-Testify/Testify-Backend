package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Essay;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EssayQuestionRepository extends JpaRepository<Essay, Long> {
}

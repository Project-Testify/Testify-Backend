package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.RandomOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RandomOrderRepository extends JpaRepository<RandomOrder, Long> {
    Optional<RandomOrder> findByExamId(long examId);
}

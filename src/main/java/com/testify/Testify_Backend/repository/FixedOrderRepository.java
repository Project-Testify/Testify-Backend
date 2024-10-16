package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.FixedOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FixedOrderRepository extends JpaRepository<FixedOrder, Long> {
    Optional<FixedOrder> findByExamId(long examId);
}

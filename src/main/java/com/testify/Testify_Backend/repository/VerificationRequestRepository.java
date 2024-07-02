package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.VerificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {
}

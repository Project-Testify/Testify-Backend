package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendeeRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String email);
}

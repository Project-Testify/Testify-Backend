package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
}

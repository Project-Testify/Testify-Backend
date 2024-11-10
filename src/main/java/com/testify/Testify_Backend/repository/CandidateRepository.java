package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    Optional<Candidate> findByEmail(String currentUserEmail);
    boolean existsByEmail(String currentUserEmail);
    Set<Candidate> findAllByEmailIn(List<String> emails);}

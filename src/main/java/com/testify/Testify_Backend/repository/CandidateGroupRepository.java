package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.CandidateGroup;
import com.testify.Testify_Backend.responses.CandidateGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface CandidateGroupRepository extends JpaRepository<CandidateGroup, Long> {
    Set<CandidateGroup> findByOrganizationId(long organizationId);
}

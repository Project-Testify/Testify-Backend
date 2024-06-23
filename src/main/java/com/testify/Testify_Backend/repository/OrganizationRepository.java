package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long>  {
    Optional<Organization> findByEmail(String email);
}

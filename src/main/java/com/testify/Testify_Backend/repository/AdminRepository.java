package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Integer>{
    Optional<Admin> findByEmail(String email);
}

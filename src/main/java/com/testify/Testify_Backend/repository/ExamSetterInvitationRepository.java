package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamSetterInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExamSetterInvitationRepository extends JpaRepository<ExamSetterInvitation, Long> {
    Optional<ExamSetterInvitation> findByToken(String token);
}

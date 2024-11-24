package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamSetterInvitation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface ExamSetterInvitationRepository extends JpaRepository<ExamSetterInvitation, Long> {
    Optional<ExamSetterInvitation> findByToken(String token);
    Set<ExamSetterInvitation> findByOrganizationId(Long id);
}

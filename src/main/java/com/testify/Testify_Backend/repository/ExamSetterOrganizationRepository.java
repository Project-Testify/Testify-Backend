package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.ExamSetterOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamSetterOrganizationRepository extends JpaRepository<ExamSetterOrganization, String> {
//    write a method to update is deleted to true by organizationID and examSetterID
//    write a method to find by organizationID and examSetterID
    ExamSetterOrganization findByOrganizationIDAndExamSetterID(String organizationID, String examSetterID);
    List<ExamSetterOrganization> findByOrganizationID(String organizationID);
    ExamSetterOrganization findByExamSetterID(String examSetterID);

    @Query("SELECT e.examSetterID FROM ExamSetterOrganization e WHERE e.organizationID = :organizationId AND e.isDeleted = false")
    List<String> findActiveExamSetterIDs(@Param("organizationId") String organizationId);

}

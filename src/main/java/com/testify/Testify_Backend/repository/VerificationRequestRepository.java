package com.testify.Testify_Backend.repository;

import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Long> {

    @Query("SELECT new com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse(o.id, o.firstName, o.addressLine1, o.addressLine2, o.city, o.state, o.website, o.profileImage, vr.verificationDocument01Url, vr.verificationDocument02Url, vr.verificationDocument03Url, vr.verificationDocument04Url, vr.verificationDocument05Url, vr.verificationStatus, vr.rejectionReason, vr.requestDate) " +
            "FROM VerificationRequest vr JOIN vr.organization o on o.id = vr.organization.id WHERE vr.verificationStatus = 'PENDING'")
    List<OrganizationGroupResponse> findVerificationRequestWithOrganizationByVerificationStatus();

    VerificationRequest findByOrganizationId(int id);
}

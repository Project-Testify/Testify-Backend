package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.UserRepository;
import com.testify.Testify_Backend.repository.VerificationRequestRepository;
import com.testify.Testify_Backend.requests.admin_management.RejectOrganizationRequest;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private  final VerificationRequestRepository verificationRequestRepository;

    @Override
    public List<OrganizationGroupResponse> getOrganizationGroup() {
        //OrganizationGroupResponse response = new OrganizationGroupResponse();
        List<OrganizationGroupResponse> res = verificationRequestRepository.findVerificationRequestWithOrganizationByVerificationStatus();

        return res;
//        List<Organization> organization = organizationRepository.findByVerifiedFalse();
//        return organization;
    }

    @Override
    public int verifyOrganization(int id) {
        int response = userRepository.verifyUser(id);
//        set verification status to verified in verficationRequest table
        VerificationRequest verificationRequest = verificationRequestRepository.findByOrganizationId(id);
        verificationRequest.setVerificationStatus("VERIFIED");
        verificationRequestRepository.save(verificationRequest);
        return response;
    }

    @Override
    public int rejectOrganization(RejectOrganizationRequest rejectOrganizationRequest) {
        int res = userRepository.unVerifyUser(rejectOrganizationRequest.getId());
        VerificationRequest verificationRequest = verificationRequestRepository.findByOrganizationId(rejectOrganizationRequest.getId());
        verificationRequest.setVerificationStatus("REJECTED");
        verificationRequest.setRejectionReason(rejectOrganizationRequest.getRejectionReason());
        verificationRequestRepository.save(verificationRequest);

        return res;
    }
}

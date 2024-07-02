package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.VerificationRequestRepository;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{
    private final OrganizationRepository organizationRepository;
    private final ExamSetterRepository examSetterRepository;
    private final VerificationRequestRepository verificationRequestRepository;

    public GenericAddOrUpdateResponse addSetterToOrganization(long organizationId, Set<Long> setterId) {
        Organization organization = organizationRepository.findById(organizationId).get();
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse<>();

        for(Long setter: setterId){
            ExamSetter examSetter = examSetterRepository.findById(setter).get();
            Set<ExamSetter> examSetters = organization.getExamSetters();
            examSetters.add(examSetter);
            //System.out.println(setter);
            Set<Organization> organizations = examSetter.getOrganizations();
            organizations.add(organization);

            examSetterRepository.save(examSetter);
        }
        organizationRepository.save(organization);

        response.setSuccess(true);
        response.setMessage("Setters added successfully");

        return response;
    }

    public GenericDeleteResponse deleteSetterFromOrganization(long organizationId, Long setterId) {
        Organization organization = organizationRepository.findById(organizationId).get();
        ExamSetter examSetter = examSetterRepository.findById(setterId).get();
        GenericDeleteResponse response = new GenericDeleteResponse();

        Set<ExamSetter> examSetters = organization.getExamSetters();
        examSetters.remove(examSetter);
        Set<Organization> organizations = examSetter.getOrganizations();
        organizations.remove(organization);

        organizationRepository.save(organization);
        examSetterRepository.save(examSetter);

        response.setSuccess(true);
        response.setMessage("Setter deleted successfully");

        return response;
    }

    public Set<ExamSetter> getSetterFromOrganization(long organizationId) {
        Organization organization = organizationRepository.findById(organizationId).get();
        Set<ExamSetter> examSetters = organization.getExamSetters();

        return examSetters;
    }

    public GenericAddOrUpdateResponse<VerificationRequestRequest> requestVerification(VerificationRequestRequest verificationRequest) throws IOException {
        GenericAddOrUpdateResponse<VerificationRequestRequest> response = new GenericAddOrUpdateResponse<>();
        MultipartFile document = verificationRequest.getVerificationDocument();

        String documentUrl = FileUploadUtil.saveFile(document, "verificationDocument");

        VerificationRequest verification = VerificationRequest.builder()
                .verificationDocumentUrl(documentUrl)
                //TODO: add proper verification status
                .verificationStatus("PENDING")
                //Todo: get organization id from logged in user
                .organizationId(1)
                .requestDate(new Date())
                .build();

        verificationRequestRepository.save(verification);

        response.setSuccess(true);
        response.setMessage("Verification request sent successfully");
        return response;
    }
}

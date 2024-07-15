package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.CourseModule;
import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.repository.CourseModuleRepository;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.VerificationRequestRepository;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import com.testify.Testify_Backend.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService{
    private final OrganizationRepository organizationRepository;
    private final ExamSetterRepository examSetterRepository;
    private final VerificationRequestRepository verificationRequestRepository;
    private final CourseModuleRepository courseModuleRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(long organizationId, AddExamSetterRequest request) {

        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();

        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        ExamSetter examSetter = examSetterRepository.findById(request.getExamSetterId())
                .orElseThrow(() -> new IllegalArgumentException("ExamSetter not found"));


        // Check if the ExamSetter is already associated with the Organization
        if (!organization.getExamSetters().contains(examSetter)) {
            organization.getExamSetters().add(examSetter);
            organizationRepository.save(organization);
        }

        // Get the CourseModules from the provided IDs
        Set<CourseModule> courseModules = new HashSet<>();
        for (Long courseModuleId : request.getCourseModuleIds()) {
            CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                    .orElseThrow(() -> new IllegalArgumentException("CourseModule not found"));
            courseModules.add(courseModule);

            // Check if the CourseModule is already associated with the ExamSetter
            if (!examSetter.getCourseModules().contains(courseModule)) {
                examSetter.getCourseModules().add(courseModule);
            }
        }

        // Update the ExamSetter with the new CourseModules
        examSetterRepository.save(examSetter);
        response.setSuccess(true);
        response.setMessage("ExamSetter added to Organization successfully");
        return ResponseEntity.ok(response);

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

    public GenericAddOrUpdateResponse<CourseModule> addCourseModuleToOrganization(long organizationId, CourseModuleRequest courseModuleRequest) {
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new RuntimeException("Organization not found"));
        CourseModule courseModule = CourseModule.builder()
                .moduleCode(courseModuleRequest.getModuleCode())
                .name(courseModuleRequest.getName())
                .organization(organization)
                .build();

        courseModuleRepository.save(courseModule);

        GenericAddOrUpdateResponse<CourseModule> response = new GenericAddOrUpdateResponse<>();
        response.setSuccess(true);
        response.setMessage("Course module added successfully");
        response.setId(courseModule.getId());

        return response;
    }

    @Override
    public Set<CourseModuleResponse> getCourseModulesByOrganization(Long organizationId) {
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));
        return organization.getCourseModules().stream()
                .map(courseModule -> modelMapper.map(courseModule, CourseModuleResponse.class))
                .collect(Collectors.toSet());
    }
}

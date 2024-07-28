package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.VerificationRequestRequest;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;
import com.testify.Testify_Backend.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationServiceImpl implements OrganizationService{
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final ExamSetterRepository examSetterRepository;
    private final VerificationRequestRepository verificationRequestRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final CandidateRepository candidateRepository;
    private final CandidateGroupRepository candidateGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(long organizationId, AddExamSetterRequest request) {
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();

        try {
            // Get organizationId from request

            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

            // Check if an exam setter with the given email exists
            ExamSetter examSetter = examSetterRepository.findByEmail(request.getEmail()).orElse(null);

            if (examSetter == null) {
                // If the exam setter doesn't exist, create a new one
                examSetter = ExamSetter.builder()
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword())) // Ensure password is encoded before saving
                        .username(request.getUsername())
                        .contactNo(request.getContactNo())
                        .role(request.getRole())
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .bio(request.getBio())
                        .organizations(new HashSet<>())
                        .build();
            }

            // Add the organization to the exam setter's list of organizations
            examSetter.getOrganizations().add(organization);

            // Save the exam setter
            examSetterRepository.save(examSetter);

            // Add the exam setter to the organization's list of exam setters
            organization.getExamSetters().add(examSetter);
            organizationRepository.save(organization);

            response.setSuccess(true);
            response.setMessage("ExamSetter added to Organization successfully");
            response.setId(examSetter.getId());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("An error occurred while adding the ExamSetter to the Organization");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }

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
    @Override
    public GenericAddOrUpdateResponse<CandidateGroupRequest> createCandidateGroup(long organizationId, CandidateGroupRequest candidateGroupRequest){

        GenericAddOrUpdateResponse<CandidateGroupRequest> response = new GenericAddOrUpdateResponse<>();

        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        Set<Candidate> candidates = new HashSet<>();
        if (candidateGroupRequest.getCandidateIds() != null) {
            List<Candidate> foundCandidates = candidateRepository.findAllById(candidateGroupRequest.getCandidateIds());
            candidates.addAll(foundCandidates);
        }

        CandidateGroup candidateGroup = CandidateGroup.builder()
                .name(candidateGroupRequest.getName())
                .organization(organization)
                .candidates(candidates)
                .build();
        candidateGroupRepository.save(candidateGroup);

        response.setSuccess(true);
        response.setMessage("Candidate group created successfully");
        response.setId(candidateGroup.getId());
        return response;
    }


}

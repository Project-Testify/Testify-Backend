package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.organization_management.AddExamSetterRequest;
import com.testify.Testify_Backend.requests.organization_management.CandidateGroupRequest;
import com.testify.Testify_Backend.requests.organization_management.CourseModuleRequest;
import com.testify.Testify_Backend.requests.organization_management.InviteExamSetterRequest;
import com.testify.Testify_Backend.responses.CandidateGroupResponse;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import com.testify.Testify_Backend.responses.courseModule.CourseModuleResponse;

import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.organization_management.ExamSetterSearchResponse;

//import com.testify.Testify_Backend.utils.FileUploadUtil;

import com.testify.Testify_Backend.utils.FileUtil;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
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
    private final ExamSetterInvitationRepository examSetterInvitationRepository;
    private final ExamRepository examRepository;
    private final EmailSender emailSender;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private ExamSetterOrganizationRepository examSetterOrganizationRepository;

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


//    public GenericAddOrUpdateResponse<VerificationRequestRequest> requestVerification(VerificationRequestRequest verificationRequest) throws IOException {
//        GenericAddOrUpdateResponse<VerificationRequestRequest> response = new GenericAddOrUpdateResponse<>();
//        MultipartFile document = verificationRequest.getVerificationDocument();
//
//        String documentUrl = FileUtil.saveFile(document, "verificationDocument");
//
//        VerificationRequest verification = VerificationRequest.builder()
//                .verificationDocumentUrl(documentUrl)
//                //TODO: add proper verification status
//                .verificationStatus("PENDING")
//                //Todo: get organization id from logged in user
//                .organizationId(1)
//                .requestDate(new Date())
//                .build();
//
//        verificationRequestRepository.save(verification);
//
//        response.setSuccess(true);
//        response.setMessage("Verification request sent successfully");
//        return response;
//    }

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
    @Transactional
    public GenericAddOrUpdateResponse<CandidateGroupRequest> createCandidateGroup(long organizationId, CandidateGroupRequest candidateGroupRequest){

        GenericAddOrUpdateResponse<CandidateGroupRequest> response = new GenericAddOrUpdateResponse<>();

        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        Set<Candidate> candidates = new HashSet<>();
        candidates = candidateRepository.findAllByEmailIn(candidateGroupRequest.getEmails());

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


    @Transactional
    public Set<CandidateGroupResponse> getCandidateGroupsByOrganization(Long organizationId) {
        Set<CandidateGroupResponse> candidateGroupsResponse = new HashSet<>();
        Set<CandidateGroup> candidateGroups = new HashSet<>();
        Organization organization = organizationRepository.findById(organizationId).orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        candidateGroups = candidateGroupRepository.findByOrganizationId(organizationId);

        return candidateGroups.stream().map(candidateGroup -> modelMapper.map(candidateGroup, CandidateGroupResponse.class)).collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public GenericAddOrUpdateResponse addCandidateToGroup(long groupId, String name, String email) {
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse<>();
        CandidateGroup candidateGroup = candidateGroupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        Candidate candidate = candidateRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Candidate not found"));
        candidateGroup.getCandidates().add(candidate);
        candidateGroupRepository.save(candidateGroup);
        response.setSuccess(true);
        response.setMessage("Candidate added successfully");
        response.setId(candidate.getId());
        return response;
    }

    @Override
    @Transactional
    public GenericDeleteResponse deleteGroup(long groupId) {
        CandidateGroup candidateGroup = candidateGroupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        candidateGroupRepository.delete(candidateGroup);
        GenericDeleteResponse response = new GenericDeleteResponse();
        response.setSuccess(true);
        response.setMessage("Candidate group deleted successfully");
        return response;
    }

    @Override
    @Transactional
    public GenericDeleteResponse deleteCandidate(long groupId, long candidateId) {
        CandidateGroup candidateGroup = candidateGroupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        candidateGroup.getCandidates().removeIf(candidate -> candidate.getId() == candidateId);
        candidateGroupRepository.save(candidateGroup);
        GenericDeleteResponse response = new GenericDeleteResponse();
        response.setSuccess(true);
        response.setMessage("Candidate group deleted successfully");
        return response;
    }

    @Override
    @Transactional
    public GenericAddOrUpdateResponse updateCandidateGroup(long groupId, String groupName) {
        CandidateGroup candidateGroup = candidateGroupRepository.findById(groupId).orElseThrow(() -> new IllegalArgumentException("Group not found"));
        candidateGroup.setName(groupName);
        candidateGroupRepository.save(candidateGroup);
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();
        response.setSuccess(true);
        response.setMessage("Candidate group updated successfully");
        return response;
    }

    @Override
    public Set<ExamSetter> getExamSetters(long organizationId) {
        // Fetch the organization and its associated ExamSetters
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

        // Fetch active ExamSetter IDs from ExamSetterOrganization table
        List<String> activeExamSetterIDs = examSetterOrganizationRepository.findActiveExamSetterIDs(String.valueOf(organizationId));

        // Filter ExamSetters linked to the organization based on active IDs
        return organization.getExamSetters().stream()
                .filter(examSetter -> activeExamSetterIDs.contains(String.valueOf(examSetter.getId())))
                .collect(Collectors.toSet());

    }

    @Override
    public Set<ExamSetterInvitation> getExamSetterInvitations(long organizationId) {
        Set<ExamSetterInvitation> invitations = examSetterInvitationRepository.findByOrganizationId(organizationId);
        return invitations;
    }

    @Transactional
    @Override
    public Set<ExamResponse> getExams(long organizationId) {
        Set<ExamResponse> examsResponses = new HashSet<>();
        Set<Exam> exams = examRepository.findByOrganizationId(organizationId);
        exams.stream().map(exam -> modelMapper.map(exam, ExamResponse.class)).forEach(examsResponses::add);
        return examsResponses;
    }

    @Override
    public ResponseEntity<GenericAddOrUpdateResponse> inviteExamSetter(long organizationId, InviteExamSetterRequest request) {
        try {
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found"));

            String token = UUID.randomUUID().toString();
            String invitationLink = "http://127.0.0.1:4500/auth/signup/examSetter?invitation=" + token;
            log.info("Invitation link: " + invitationLink);

            ExamSetterInvitation invitation = new ExamSetterInvitation();
            invitation.setEmail(request.getEmail());
            invitation.setOrganization(organization);
            invitation.setInvitationLink(invitationLink);
            invitation.setToken(token);
            invitation.setAccepted(false);

            examSetterInvitationRepository.save(invitation);

            emailSender.send(
                    request.getEmail(),
                    buildEmail(invitationLink)
            );

            GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();
            response.setSuccess(true);
            response.setMessage("Invitation sent successfully");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();
            response.setSuccess(false);
            response.setMessage(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();
            response.setSuccess(false);
            response.setMessage("An error occurred while sending the invitation");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String buildEmail(String invitationLink) {
        return "You have been invited to join the organization. Click the link to register: " +
                invitationLink;
    }

    public String confirmToken(String Token){
        ExamSetterInvitation invitation = examSetterInvitationRepository.findByToken(Token).orElseThrow(() -> new IllegalArgumentException("Invitation not found"));
        invitation.setAccepted(true);
        examSetterInvitationRepository.save(invitation);
        return "Invitation accepted";
    }

    @Transactional
    public ResponseEntity<?> getExamSettersForSearchByOrganizationId(Long organizationId) {
        try {
            // Find the organization by ID
            Organization organization = organizationRepository.findById(organizationId)
                    .orElseThrow(() -> new IllegalArgumentException("Organization not found with id: " + organizationId));

            log.debug("Organization: " + organization);

            // Get the set of exam setters
            Set<ExamSetter> examSetters = organization.getExamSetters();
            log.info("Exam setters: " + examSetters);

            // Map each ExamSetter to ExamSetterSearchResponse and collect as a List
            List<ExamSetterSearchResponse> responseList = examSetters.stream()
                    .map(examSetter -> new ExamSetterSearchResponse(
                            examSetter.getId(),
                            examSetter.getFirstName(),
                            examSetter.getLastName(),
                            examSetter.getEmail()
                    ))
                    .collect(Collectors.toList());

            // Return a 200 OK response with the list of exam setters
            return ResponseEntity.ok(responseList);
        } catch (IllegalArgumentException e) {
            // Return a 404 NOT FOUND response with an error message
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other exceptions with a 500 INTERNAL SERVER ERROR response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
        }
    }






}

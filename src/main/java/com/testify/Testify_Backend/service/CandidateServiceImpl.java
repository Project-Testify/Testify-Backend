package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.ExamStatus;
import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.CandidateRepository;
import com.testify.Testify_Backend.repository.ExamRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.UserRepository;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateProfile;
import com.testify.Testify_Backend.responses.candidate_management.OrganizationCandidateView;
import com.testify.Testify_Backend.utils.VarList;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CandidateServiceImpl implements CandidateService {

    @Autowired
    private final CandidateRepository candidateRepository;

    @Autowired
    private final ModelMapper modelMapper;

    @Autowired
    private final ExamRepository examRepository;

    @Autowired
    private final OrganizationRepository organizationRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private GenericResponse genericResponse;

    @Override
    public List<CandidateExam> getCandidateExams(String status) {
        // Get the current user's email (you can adapt this to your actual method of getting the logged-in user's email)
        String currentUserEmail = "testcan@gmail.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));

        // Get exams directly associated with the candidate
        List<Exam> candidateExams = examRepository.findExamsByCandidateId(candidate.getId());

        // Get all public exams
        List<Exam> publicExams = examRepository.findAllPublicExams();

        // Combine both lists, ensuring no duplicates
        Set<Exam> allExams = new HashSet<>(candidateExams);
        allExams.addAll(publicExams);

        if (allExams.isEmpty()) {
            return new ArrayList<>();
        } else {
            LocalDateTime now = LocalDateTime.now();

            // Filter exams based on the provided status
            return allExams.stream()
                    .map(exam -> {
                        return getCandidateExam(exam, now);
                    })
                    .filter(candidateExam -> {
                        // Apply the status filter
                        if (status == null) {
                            return true;
                        }
                        return candidateExam.getStatus().name().equalsIgnoreCase(status);
                    })
                    .collect(Collectors.toList());
        }

    }

    @Override
    public CandidateExam getCandidateExamDetails(Integer examId) {
        String currentUserEmail = "testcan@gmail.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));

        Exam exam = examRepository.findById((long) examId)
                .orElseThrow(() -> new EntityNotFoundException("Exam not found"));

        boolean isCandidateExam = examRepository.findExamsByCandidateId(candidate.getId())
                .stream()
                .anyMatch(candidateExam -> candidateExam.getId() == examId);

        boolean isPublicExam = examRepository.findAllPublicExams()
                .stream()
                .anyMatch(publicExam -> publicExam.getId() == examId);

        if (!isCandidateExam && !isPublicExam) {
            throw new AccessDeniedException("You do not have access to this exam.");
        }

        // Determine the status of the exam
        LocalDateTime now = LocalDateTime.now();
        return getCandidateExam(exam, now);
    }

    @NotNull
    private CandidateExam getCandidateExam(Exam exam, LocalDateTime now) {
        CandidateExam candidateExam = modelMapper.map(exam, CandidateExam.class);

        if (now.isBefore(exam.getStartDatetime())) {
            candidateExam.setStatus(ExamStatus.UPCOMING);
        } else if (now.isAfter(exam.getEndDatetime())) {
            candidateExam.setStatus(ExamStatus.EXPIRED);
        } else {
            candidateExam.setStatus(ExamStatus.ONGOING);
        }

        return candidateExam;
    }

    @Override
    public List<OrganizationCandidateView> getOrganizations() {
        List<Organization> organizations = organizationRepository.findAll();

        return organizations.stream()
                .map(org -> {
                    // Check verification status in the _user table
                    boolean isVerified = userRepository.existsByEmailAndVerified(org.getEmail(), true);

                    String contactNumber = userRepository.findContactByEmail(org.getEmail()).orElse("N/A");

                    // Map to OrganizationCandidateView
                    OrganizationCandidateView view = new OrganizationCandidateView();
                    view.setEmail(org.getEmail());
                    view.setFirstName(org.getFirstName());
                    view.setAddressLine1(org.getAddressLine1());
                    view.setAddressLine2(org.getAddressLine2());
                    view.setCity(org.getCity());
                    view.setState(org.getState());
                    view.setWebsite(org.getWebsite());

                    // Set verified status (add a custom setter or handle separately if needed)
                    view.setVerified(isVerified);
                    view.setContact(contactNumber);

                    return view;
                })
                .toList();
    }


    @Override
    public CandidateProfile getCandidateProfile(){
        String currentUserEmail = "john.doe@example.com";
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail).get();

        return modelMapper.map(candidate, CandidateProfile.class);
    }

    @Override
    public String updateCandidateProfile(Candidate candidate){
        if(candidateRepository.existsByEmail(candidate.getEmail())){
            candidateRepository.save(candidate);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }

    @Override
    public String deleteCandidateProfile(long id){
        if(candidateRepository.existsById(id)){
            candidateRepository.deleteById(id);
            return VarList.RSP_SUCCESS;
        }else{
            return VarList.RSP_NO_DATA_FOUND;
        }
    }



}
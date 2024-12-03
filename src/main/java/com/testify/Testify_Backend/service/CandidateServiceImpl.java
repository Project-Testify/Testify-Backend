package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.ExamStatus;
import com.testify.Testify_Backend.model.Candidate;
import com.testify.Testify_Backend.model.CandidateExamSession;
import com.testify.Testify_Backend.model.Exam;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.candidate_management.CandidateExam;
import com.testify.Testify_Backend.responses.candidate_management.CandidateResponse;
import com.testify.Testify_Backend.utils.UserUtil;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private final ExamSessionRepository examSessionRepository;

    @Override
    public List<CandidateExam> getCandidateExams(String status) {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (currentUserEmail == null) {
            throw new IllegalStateException("No authenticated user found");
        }
        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new EntityNotFoundException("Candidate not found"));


        List<Exam> candidateExams = examRepository.findExamsByCandidateId(candidate.getId());


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
                        CandidateExamSession candidateExamSession = examSessionRepository.findByExamIdAndCandidateId(exam.getId(), candidate.getId())
                                .orElse(null);
                        return getCandidateExam(exam, now, candidateExamSession);
                    })
                    .filter(candidateExam -> {
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
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (currentUserEmail == null) {
            throw new IllegalStateException("No authenticated user found");
        }

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
        CandidateExamSession candidateExamSession = examSessionRepository.findByExamIdAndCandidateId(exam.getId(), candidate.getId())
                .orElse(null);
        return getCandidateExam(exam, now, candidateExamSession);
    }

    @NotNull
    private CandidateExam getCandidateExam(Exam exam, LocalDateTime now, CandidateExamSession session) {
        CandidateExam candidateExam = modelMapper.map(exam, CandidateExam.class);

        if (now.isBefore(exam.getStartDatetime())) {
            candidateExam.setStatus(ExamStatus.UPCOMING);
        } else if (session == null && now.isAfter(exam.getEndDatetime())) {
            candidateExam.setStatus(ExamStatus.EXPIRED);
        } else if (session == null) {
            candidateExam.setStatus(ExamStatus.AVAILABLE);
        } else if (session.getInProgress()) {
            candidateExam.setStatus(ExamStatus.ONGOING);
        } else {
            candidateExam.setStatus(ExamStatus.COMPLETED);
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
    public CandidateProfile getCandidateProfile() {
        // Get the username (email) from SecurityContextHolder
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();

        if (currentUserEmail == null) {
            throw new IllegalStateException("No authenticated user found");
        }

        Candidate candidate = candidateRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found for email: " + currentUserEmail));

        // Map and return CandidateProfile
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

    public List<CandidateResponse> getAllCandidatesForSearch() {
        List<Candidate> candidates = candidateRepository.findAll();

        if (candidates.isEmpty()) {
            return null;
        }

        return candidates.stream()
                .map(candidate -> new CandidateResponse(
                        candidate.getId(),
                        candidate.getFirstName(),
                        candidate.getLastName(),
                        candidate.getEmail()
                        ))
                .collect(Collectors.toList());
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
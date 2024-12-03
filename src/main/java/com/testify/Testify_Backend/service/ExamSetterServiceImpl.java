package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.CandidateResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ExamSetterServiceImpl implements ExamSetterService {
    private  final ExamSetterRepository examSetterRepository;
    private final ExamSetterInvitationRepository examSetterInvitationRepository;
    private final OrganizationRepository organizationRepository;
    private final ExamRepository examRepository;
    private final CandidateRepository candidateRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Set<OrganizationResponse> getOrganizations(long setterId) {
        Optional<ExamSetter> examSetter = examSetterRepository.findById(setterId);
        Set<OrganizationResponse> organizationResponses = new HashSet<>();
        if (examSetter.isPresent()) {
            Set<Organization> organizations = examSetter.get().getOrganizations();
            for (Organization organization : organizations) {
                organizationResponses.add(modelMapper.map(organization, OrganizationResponse.class));
            }
        }
        return organizationResponses;
    }

    @Override
    public long checkSetterRegistration(String token) {
        long setterId = 0;
        Optional<ExamSetterInvitation> invitation = examSetterInvitationRepository.findByToken(token);
        String email = invitation.get().getEmail();
        Optional<ExamSetter> examSetter = examSetterRepository.findByEmail(email);
        if (examSetter.isPresent()) {
            setterId = examSetter.get().getId();
        }
        return setterId;
    }

    @Override
    public GenericAddOrUpdateResponse addSetterToOrganization(String token) {
        GenericAddOrUpdateResponse response = new GenericAddOrUpdateResponse();
        Optional<ExamSetterInvitation> examSetterInvitation =  examSetterInvitationRepository.findByToken(token);
        Organization organization = examSetterInvitation.get().getOrganization();
        examSetterInvitation.get().setAccepted(true);
        ExamSetter examSetter = examSetterRepository.findByEmail(examSetterInvitation.get().getEmail()).get();
        examSetter.getOrganizations().add(organization);
        organization.getExamSetters().add(examSetter);

        examSetterRepository.save(examSetter);
        organizationRepository.save(organization);

        response.setSuccess(true);
        response.setMessage("Successfully added an organization");
        return response;
    }

    @Override
    @Transactional
    public Set<ExamResponse> getExamsForProctor(Long proctorId, Long organizationId) {
        Set<Exam> exams = examRepository.findByProctorIdAndOrganizationId(proctorId, organizationId);
        Set<ExamResponse> examResponses = new HashSet<>();
        for (Exam exam : exams) {
            examResponses.add(modelMapper.map(exam, ExamResponse.class));
        }
        return examResponses;
    }

    @Override
    public Set<CandidateResponse> getCandidatesForExam(Long examId) {
        Set<Candidate> candidates = candidateRepository.findByExamId(examId);
        Set<CandidateResponse> candidateResponses = new HashSet<>();
        for (Candidate candidate : candidates) {
            candidateResponses.add(modelMapper.map(candidate, CandidateResponse.class));
        }
        return candidateResponses;
    }
}

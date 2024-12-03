package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.ExamSetterInvitation;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.ExamRepository;
import com.testify.Testify_Backend.repository.ExamSetterInvitationRepository;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.ExamResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import com.testify.Testify_Backend.responses.examsetter_management.ModerateExamResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExamSetterServiceImpl implements ExamSetterService {
    private  final ExamSetterRepository examSetterRepository;
    private final ExamSetterInvitationRepository examSetterInvitationRepository;
    private final OrganizationRepository organizationRepository;
    private final ExamRepository examRepository;
    
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

    @Transactional
    public List<ModerateExamResponse> getModeratingExams(long examSetterId) {
        return examRepository.findByModeratorId(examSetterId).stream()
                .map(exam -> new ModerateExamResponse(
                        exam.getId(),
                        exam.getTitle(),
                        exam.getStartDatetime(),
                        exam.getEndDatetime()
                ))
                .collect(Collectors.toList());
    }
}

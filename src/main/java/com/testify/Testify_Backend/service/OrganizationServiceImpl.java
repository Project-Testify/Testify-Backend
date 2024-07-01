package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{
    private final OrganizationRepository organizationRepository;
    private final ExamSetterRepository examSetterRepository;

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
}

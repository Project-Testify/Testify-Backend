package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.ExamSetterRepository;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService{
    private final OrganizationRepository organizationRepository;
    private final ExamSetterRepository examSetterRepository;

    public void addSetterToOrganization(long organizationId, Set<Long> setterId) {
        Organization organization = organizationRepository.findById(organizationId).get();

        for(Long setter: setterId){
            ExamSetter examSetter = examSetterRepository.findById(setter).get();
            Set<ExamSetter> examSetters = organization.getExamSetters();
            examSetters.add(examSetter);
            System.out.println(setter);
            Set<Organization> organizations = examSetter.getOrganizations();
            organizations.add(organization);

            examSetterRepository.save(examSetter);
        }
        organizationRepository.save(organization);

    }
}

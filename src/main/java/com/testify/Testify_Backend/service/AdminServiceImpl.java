package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final OrganizationRepository organizationRepository;

    @Override
    public List<Organization> getOrganizationGroup() {
        //OrganizationGroupResponse response = new OrganizationGroupResponse();

        List<Organization> organization = organizationRepository.findAll();
        return organization;
    }
}

package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService{
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Override
    public List<Organization> getOrganizationGroup() {
        //OrganizationGroupResponse response = new OrganizationGroupResponse();

        List<Organization> organization = organizationRepository.findByVerifiedFalse();
        return organization;
    }

    @Override
    public int verifyOrganization(int id) {

        int response = userRepository.verifyUser(id);
        return response;
    }

    @Override
    public int rejectOrganization(int id) {
        return userRepository.unVerifyUser(id);
    }
}

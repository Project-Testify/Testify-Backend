package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;
import com.testify.Testify_Backend.service.AdminService;
import com.testify.Testify_Backend.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
@Slf4j
@ToString(callSuper = true)
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/getOrganizationRequests")
    public ResponseEntity<List<OrganizationGroupResponse>> getOrganizations() {
            List<Organization> organizations = adminService.getOrganizationGroup();

            List<OrganizationGroupResponse> organizationGroupResponses = organizations.stream().map( org ->
                    new OrganizationGroupResponse(
                            org.getId(),
                            org.getFirstName(),
                            org.getAddressLine1(),
                            org.getAddressLine2(),
                            org.getCity(),
                            org.getState(),
                            org.getBio(),
                            org.getWebsite()
                    )
                    ).collect(Collectors.toList());

        log.info("controleer : " + organizationGroupResponses);

        return ResponseEntity.ok(organizationGroupResponses);
    }
}

package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.Organization;
import com.testify.Testify_Backend.model.VerificationRequest;
import com.testify.Testify_Backend.repository.OrganizationRepository;
import com.testify.Testify_Backend.requests.admin_management.RejectOrganizationRequest;
import com.testify.Testify_Backend.responses.GenericResponse;
import com.testify.Testify_Backend.responses.admin.OrganizationGroupResponse;
import com.testify.Testify_Backend.service.AdminService;
import com.testify.Testify_Backend.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
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
            log.info("getOrganizations - start");
            List<OrganizationGroupResponse> organizations = adminService.getOrganizationGroup();

        return ResponseEntity.ok(organizations);
    }

    @PatchMapping("/verifyOrganization/{userId}")
    public ResponseEntity verifyOrganization(@PathVariable int userId) {
        int response = adminService.verifyOrganization(userId);
        log.info("controller : {}", response);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/rejectOrganization")
    public ResponseEntity rejectOrganization(@RequestBody RejectOrganizationRequest rejectOrganizationRequest) {

        int response = adminService.rejectOrganization(rejectOrganizationRequest);
        log.info("controller : {}", response);

        return ResponseEntity.ok(response);
    }
}

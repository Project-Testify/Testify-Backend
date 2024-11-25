package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.exam_management.OrganizationResponse;
import com.testify.Testify_Backend.service.ExamSetterService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/exam-setter")
@RequiredArgsConstructor
public class ExamSetterController {
    private static final Logger log = LoggerFactory.getLogger(ExamSetterController.class);
    private final ExamSetterService examSetterService;

    @GetMapping("/{setterId}/getOrganizations")
    public ResponseEntity<Set<OrganizationResponse>> getOrganizations(@PathVariable("setterId") long setterId) {
        Set<OrganizationResponse> organizations = examSetterService.getOrganizations(setterId);
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{token}/checkSetterRegistration")
    public ResponseEntity<Long> checkSetterRegistration(@PathVariable("token") String token) {
        long setterId = examSetterService.checkSetterRegistration(token);
        return ResponseEntity.ok(setterId);
    }

    @PostMapping("/{token}/addSetterToOrganization")
    public ResponseEntity<GenericAddOrUpdateResponse> addSetterToOrganization(@PathVariable("token") String token){
        log.info("setter reached: {}",token);
        GenericAddOrUpdateResponse response = examSetterService.addSetterToOrganization(token);
        return ResponseEntity.ok(response);
    }
}

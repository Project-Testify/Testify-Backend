package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.service.OrganizationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private final OrganizationServiceImpl organizationService;

    @PostMapping("/{organizationId}/addSetter")
    public String addSetterToOrganization(@PathVariable long organizationId, @RequestBody Set<Long> setterId){
        organizationService.addSetterToOrganization(organizationId, setterId);
        return "Setter Added Successfully";
    }
}

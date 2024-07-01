package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.model.ExamSetter;
import com.testify.Testify_Backend.responses.GenericAddOrUpdateResponse;
import com.testify.Testify_Backend.responses.GenericDeleteResponse;
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
    public GenericAddOrUpdateResponse addSetterToOrganization(@PathVariable long organizationId, @RequestBody Set<Long> setterId){
        return organizationService.addSetterToOrganization(organizationId, setterId);

    }

    @DeleteMapping("/{organizationId}/deleteSetter")
    public GenericDeleteResponse deleteSetterFromOrganization(@PathVariable long organizationId, @RequestBody Long setterId){
        return organizationService.deleteSetterFromOrganization(organizationId, setterId);
    }

    @GetMapping("/{organizationId}/getSetter")
    public Set<ExamSetter> getSetterFromOrganization(@PathVariable long organizationId){
        return organizationService.getSetterFromOrganization(organizationId);
    }
}

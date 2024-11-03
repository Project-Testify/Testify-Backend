package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.responses.RoleWiseStats;
import com.testify.Testify_Backend.responses.reports.UserRegistrationStats;
import com.testify.Testify_Backend.service.UserReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@Slf4j
public class UserReportController {
    private final UserReportService userReportService;

    @GetMapping("/registrations")
    public ResponseEntity<List<UserRegistrationStats>> getUserRegistrations() {
        List<UserRegistrationStats> stats = userReportService.getUserRegistrationsForDate();
        return ResponseEntity.ok(stats);

    }

    @GetMapping("/roleStats")
    public ResponseEntity<List<RoleWiseStats>> getRoleWiseStats() {
        List<RoleWiseStats> res = userReportService.getRoleWisrStats();
        return ResponseEntity.ok(res);
    }
}

package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.responses.RoleWiseStats;
import com.testify.Testify_Backend.responses.reports.UserRegistrationStats;

import java.time.LocalDate;
import java.util.List;

public interface UserReportService {

    public List<UserRegistrationStats> getUserRegistrationsForDate();

    public List<RoleWiseStats> getRoleWisrStats();
}

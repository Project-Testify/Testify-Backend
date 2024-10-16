package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.enums.UserRole;
import com.testify.Testify_Backend.repository.UserRepository;
import com.testify.Testify_Backend.responses.RoleWiseStats;
import com.testify.Testify_Backend.responses.reports.UserRegistrationStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserReportServiceImpl implements UserReportService {
    @Autowired
    private UserRepository userRepository;

//    get user registration trends by userr catergory
    @Override
    public List<UserRegistrationStats> getUserRegistrationsForDate( ) {
        List<Object[]> res = userRepository.findUserRegistrationsGroupedByDateAndRole();

        // Initialize an empty list to store the mapped UserRegistrationStats
        List<UserRegistrationStats> userRegistrationStats = new ArrayList<>();

        // Iterate through the results and map each Object[] to UserRegistrationStats
        for (Object[] row : res) {
            userRegistrationStats.add(UserRegistrationStats.builder()
                    .date((Date) row[0])
                    .category((UserRole) row[1])
                    .value((Long) row[2])
                    .build());
        }

        return userRegistrationStats;
    }

    @Override
    public List<RoleWiseStats> getRoleWisrStats(){
        List<Object[]> res = userRepository.findByRoleGroupByRole();

        List<RoleWiseStats> roleWiseStats = new ArrayList<>();
        for (Object[] row : res){
            roleWiseStats.add(RoleWiseStats.builder()
                            .role((UserRole) row[0])
                            .count((Long) row[1])
                    .build());
        }
        return roleWiseStats;
    }
}

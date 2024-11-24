package com.testify.Testify_Backend.responses.reports;


import com.testify.Testify_Backend.enums.UserRole;
import lombok.*;

import java.sql.Date;


@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserRegistrationStats {
    private Date date;
    private UserRole category; // Ensure this is the correct enum
    private Long value;
}

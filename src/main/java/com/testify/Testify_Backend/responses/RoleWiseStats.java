package com.testify.Testify_Backend.responses;

import com.testify.Testify_Backend.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@Builder
public class RoleWiseStats {
    private UserRole role;
    private Long count;
}

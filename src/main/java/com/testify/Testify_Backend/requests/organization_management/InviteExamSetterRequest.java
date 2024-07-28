package com.testify.Testify_Backend.requests.organization_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class InviteExamSetterRequest {
    private String email;
}

package com.testify.Testify_Backend.requests.organization_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CourseModuleRequest {
    private String moduleCode;
    private String name;
}

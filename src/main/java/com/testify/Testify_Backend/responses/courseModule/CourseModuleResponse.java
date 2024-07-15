package com.testify.Testify_Backend.responses.courseModule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseModuleResponse {
    private Long id;
    private String moduleCode;
    private String name;
}

package com.testify.Testify_Backend.responses.organization_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExamSetterSearchResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}

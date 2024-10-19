package com.testify.Testify_Backend.responses.exam_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExamSetterResponse {
    private long id;
    private String email;
    private String firstName;
    private String lastName;
}

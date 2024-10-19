package com.testify.Testify_Backend.requests.exam_management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamUpdateRequest {
    private String title;
    private String description;
    private String instructions;
    private int duration;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isPrivate;
    private Long organizationId;
}

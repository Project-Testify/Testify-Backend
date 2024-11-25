package com.testify.Testify_Backend.responses.exam_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConflictExamResponse {
    private long id;
    private String title;
    private String description;
    private String instructions;
    private int duration;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
}

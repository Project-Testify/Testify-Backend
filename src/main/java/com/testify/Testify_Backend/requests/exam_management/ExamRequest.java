package com.testify.Testify_Backend.requests.exam_management;


import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class  ExamRequest {
    private String title;
    private Long organizationId;
    private String description;
    private String instructions;
    private int duration;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private boolean isPrivate;
    private Long moderatorId;
}

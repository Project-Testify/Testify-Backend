package com.testify.Testify_Backend.responses.examsetter_management;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModerateExamResponse {
    private long id;
    private String title;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
}

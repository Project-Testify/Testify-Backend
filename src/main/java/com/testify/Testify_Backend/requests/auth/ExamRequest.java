package com.testify.Testify_Backend.requests.auth;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExamRequest {
    private String title;
    private String description;
    private String instructions;
    private int duration;
    private int totalMarks;
    private int passMarks;
    private long examSetterId;
    private long organizationId;
}

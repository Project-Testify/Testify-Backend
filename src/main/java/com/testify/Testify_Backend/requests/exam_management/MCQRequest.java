package com.testify.Testify_Backend.requests.exam_management;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MCQRequest{
    private long examId;
    private String questionText;
    private String difficultyLevel;
    private List<MCQOptionRequest> options;

}

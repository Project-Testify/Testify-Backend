package com.testify.Testify_Backend.responses.exam_management;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionSequenceResponse {
    private long examId;
    private List<Long> questionIds;
    private String errorMessage;
}

package com.testify.Testify_Backend.responses.exam_management;

import com.testify.Testify_Backend.enums.ExamType;
import com.testify.Testify_Backend.enums.QuestionType;
import com.testify.Testify_Backend.model.Question;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuestionListResponse {
    private long examId;
    private ExamType examType;
    private List<QuestionResponse> questions; // List of QuestionResponse objects
    private String errorMessage; // Optional error message if any
}

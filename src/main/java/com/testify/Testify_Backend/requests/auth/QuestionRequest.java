package com.testify.Testify_Backend.requests.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRequest {
    private String question;
    private long examId;
    private String questionType;
    private Set<OptionRequest> optionRequests; //only for MCQ type questions otherwise null
}

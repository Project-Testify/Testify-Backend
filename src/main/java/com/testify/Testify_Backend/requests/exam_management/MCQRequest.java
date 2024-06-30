package com.testify.Testify_Backend.requests.exam_management;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class MCQRequest extends QuestionRequest{
    private Set<MCQOptionRequest> options;
}

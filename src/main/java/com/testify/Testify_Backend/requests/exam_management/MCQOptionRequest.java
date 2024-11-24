package com.testify.Testify_Backend.requests.exam_management;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MCQOptionRequest {
    private String optionText;
    private boolean correct;
    private double marks;
}


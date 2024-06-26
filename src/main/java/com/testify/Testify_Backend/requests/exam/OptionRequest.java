package com.testify.Testify_Backend.requests.exam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OptionRequest {
    private String optionText;
    private boolean isCorrect;
}

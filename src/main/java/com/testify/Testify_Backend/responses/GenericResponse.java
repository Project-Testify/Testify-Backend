package com.testify.Testify_Backend.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GenericResponse {
    private String code;
    private String message;
    private Object content;
}

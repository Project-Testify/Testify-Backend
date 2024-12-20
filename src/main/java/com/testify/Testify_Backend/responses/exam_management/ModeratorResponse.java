package com.testify.Testify_Backend.responses.exam_management;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModeratorResponse {
    private String email;
    private String firstName;
    private String lastName;
}

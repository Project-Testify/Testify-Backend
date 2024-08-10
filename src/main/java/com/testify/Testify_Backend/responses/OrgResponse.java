package com.testify.Testify_Backend.responses;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class OrgResponse {
    private String name;
    private String email;
    private String phone;
}

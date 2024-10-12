package com.testify.Testify_Backend.requests.organization_management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CandidateGroupRequest {
    private String name;
    private List<String> emails;
}

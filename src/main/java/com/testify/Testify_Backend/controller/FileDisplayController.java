package com.testify.Testify_Backend.controller;

import com.testify.Testify_Backend.utils.FileUtil;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;

@RestController
@RequestMapping("/api/v1/file")
public class FileDisplayController {
    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {

        Resource resource = null;
        try {
            resource = FileUtil.loadFileAsResource(fileName, "verificationDocument");

            String contentType = Files.probeContentType(resource.getFile().toPath());

            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .header("X-Frame-Options", "SAMEORIGIN")
                    .body(resource);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

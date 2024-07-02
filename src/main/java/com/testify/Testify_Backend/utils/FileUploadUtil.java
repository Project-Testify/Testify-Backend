package com.testify.Testify_Backend.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {

    private static final String VERIFICATION_DOCUMENT_DIR = "files/verificationDocument";

    public static String saveFile(MultipartFile file, String fileType) throws IOException {
        String uploadDir;
        if ("verificationDocument".equals(fileType)) {
            uploadDir = VERIFICATION_DOCUMENT_DIR;
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path copyLocation = Paths.get(uploadDir).resolve(fileName).normalize();

        // Ensure the directory exists
        Files.createDirectories(copyLocation.getParent());

        // Copy the file to the target location
        Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}

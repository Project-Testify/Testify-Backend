package com.testify.Testify_Backend.utils;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtil {

    private static final String VERIFICATION_DOCUMENT_DIR = "files/verificationDocument";
    private static final String PROFILE_IMAGE_DIR = "files/profileImage";
    public static String saveFile(MultipartFile file, String fileType) throws IOException {
        String uploadDir;
        if ("verificationDocument".equals(fileType)) {
            uploadDir = VERIFICATION_DOCUMENT_DIR;
        } else if ("profileImage".equals(fileType)) {
            uploadDir = PROFILE_IMAGE_DIR;
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


    public static Resource loadFileAsResource(String fileName, String fileType) throws MalformedURLException {
        Path filePath;
        if ("verificationDocument".equals(fileType)) {
            filePath = Paths.get(VERIFICATION_DOCUMENT_DIR).resolve(fileName).normalize();
        } else if ("profileImage".equals(fileType)) {
            filePath = Paths.get(PROFILE_IMAGE_DIR).resolve(fileName).normalize();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fileType);
        }
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }
}

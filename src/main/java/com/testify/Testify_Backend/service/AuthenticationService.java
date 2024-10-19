package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.*;
import com.testify.Testify_Backend.repository.*;
import com.testify.Testify_Backend.requests.auth.AuthenticationRequest;
import com.testify.Testify_Backend.responses.auth.AuthenticationResponse;
import com.testify.Testify_Backend.requests.auth.RegistrationRequest;
import com.testify.Testify_Backend.responses.auth.RegisterResponse;
import com.testify.Testify_Backend.enums.TokenType;
import com.testify.Testify_Backend.enums.UserRole;

import com.testify.Testify_Backend.utils.FileUtil;
import jakarta.mail.Multipart;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final ConfirmationTokenService confirmationTokenService;
    private final UserService userService;
    private final EmailSender emailSender;
    private final TokenRepository tokenRepository;
    private final CandidateRepository attendeeRepository;
    private final ExamSetterRepository examSetterRepository;
    private final OrganizationRepository organizationRepository;
    private final VerificationRequestRepository verificationRequestRepository;
    private User user;


    //TEST UPDATE
    public RegisterResponse register(@ModelAttribute RegistrationRequest request, boolean preVerified) {

        var response = new RegisterResponse();
        System.out.println("Registration request: " + request);

        if (userService.findByEmail(request.getEmail()).isPresent()) {
            response.addError("email", "Email is already taken");
        }

        if (response.checkValidity(request)) {
//
            User savedUser = null;
            if (request.getRole().equals(UserRole.CANDIDATE)) {
                Candidate candidate = Candidate.builder()
                        .email(request.getEmail())
                        .username(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .bio(request.getBio())
                        .contactNo(request.getContactNo())
                        .role(UserRole.CANDIDATE)
                        .enabled(false)
                        .locked(false)
                        .verified(true)
                        .build();
                savedUser = attendeeRepository.save(candidate);
            }else if(request.getRole().equals(UserRole.EXAMSETTER)){
                ExamSetter examSetter = ExamSetter.builder()
                        .email(request.getEmail())
                        .username(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .bio(request.getBio())
                        .contactNo(request.getContactNo())
                        .role(UserRole.EXAMSETTER)
                        .enabled(false)
                        .locked(false)
                        .verified(true)
                        .build();
                savedUser = examSetterRepository.save(examSetter);
            } else if (request.getRole().equals(UserRole.ORGANIZATION)) {
                System.out.println("Organization");
                Organization organization = Organization.builder()
                        .email(request.getEmail())
                        .username(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .contactNo(request.getContactNo())
                        .addressLine1(request.getAddressLine1())
                        .addressLine2(request.getAddressLine2())
                        .city(request.getCity())
                        .state(request.getState())
                        .website(request.getWebsite())
                        .role(UserRole.ORGANIZATION)
                        .enabled(false)
                        .locked(false)
                        .verified(false)
                        .build();
                savedUser = organizationRepository.save(organization);

                //save verification documents
                if (request.getVerificationDocuments() != null) {
                    try {
                        MultipartFile[] uploadDocuments = request.getVerificationDocuments();
                        List<String> documentUrls = new ArrayList<>();

                        // Loop through each file and save it
                        for (MultipartFile file : uploadDocuments) {
                            if (file.isEmpty() || file.getContentType() == null) {
                                continue; // Skip empty or invalid files
                            }

                            // Save the file and add the URL to the list
                            String savedFileUrl = FileUtil.saveFile(file, "verificationDocument");
                            documentUrls.add(savedFileUrl);
                        }

                        // Use safe access in case there are fewer than 5 files
                        String document01Url = documentUrls.size() > 0 ? documentUrls.get(0) : null;
                        String document02Url = documentUrls.size() > 1 ? documentUrls.get(1) : null;
                        String document03Url = documentUrls.size() > 2 ? documentUrls.get(2) : null;
                        String document04Url = documentUrls.size() > 3 ? documentUrls.get(3) : null;
                        String document05Url = documentUrls.size() > 4 ? documentUrls.get(4) : null;

                        // Build the VerificationRequest object
                        VerificationRequest uploadedFile = VerificationRequest.builder()
                                .verificationDocument01Url(document01Url)
                                .verificationDocument02Url(document02Url)
                                .verificationDocument03Url(document03Url)
                                .verificationDocument04Url(document04Url)
                                .verificationDocument05Url(document05Url)
                                .verificationStatus("PENDING")
                                .organization(organization) // Assuming savedUser is an Organization object
                                .requestDate(new Date())
                                .build();

                        // Save the verification request
                        verificationRequestRepository.save(uploadedFile);

                    } catch (IOException e) {
                        // Handle file saving error
                        e.printStackTrace();
                        // Optionally, throw a custom exception or handle the error accordingly
                    }
                }


            }else if(request.getRole().equals(UserRole.ADMIN)){
                Admin admin = Admin.builder()
                        .email(request.getEmail())
                        .username(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .contactNo(request.getContactNo())
                        .role(UserRole.ADMIN)
                        .enabled(false)
                        .locked(false)
                        .verified(true)
                        .build();
                savedUser = userRepository.save(admin);
            }




            //TODO: Send confirmation token
            String token = UUID.randomUUID().toString();

            ConfirmationToken confirmationToken = new ConfirmationToken(
                    token,
                    LocalDateTime.now(),
                    LocalDateTime.now().plusMinutes(15),
                    savedUser
            );
            confirmationTokenService.saveConfirmationToken(confirmationToken);

            // TODO: Send email
            String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;

            // Log the confirmation link
            log.info("Confirmation link: {}", link);

//            emailSender.send(
//                    request.getEmail(),
//                    buildEmail(request.getEmail(), link)
//            );

            //TODO: save jwt token
            var jwtToken = jwtService.generateToken(savedUser);
            var refreshToken = jwtService.generateRefreshToken(savedUser);
            saveUserToken(savedUser, jwtToken);

            //TODO: set response
            response.setLoggedUser(savedUser);
            response.setRole(savedUser.getRole());
            response.setAccessToken(jwtToken);
            response.setRefreshToken(refreshToken);
            response.setSuccess(true);
        } else {
            response.setSuccess(false);
            response.setErrorMessage(
                    "Something went wrong please try again");
        }

        return response;

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false).build();
        tokenRepository.save(token);
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        userService.enableUser(
                confirmationToken.getUser().getEmail()
        );
        return "confirmed";
    }


    private String buildEmail(String name, String link) {
        return "Hi " + name + ",\n\n" +
                "Thank you for registering. Please click on the link below to activate your account:\n\n" +
                link + "\n\n" +
                "The link will expire in 15 minutes.\n\n" +
                "See you soon.";
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var response = new AuthenticationResponse();
        var user = userService.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            response.setSuccess(false);
            response.addError("email", "Email does not exist");
        } else if (!user.isEnabled()) {
            response.setSuccess(false);
            response.addError("email", "Email is not verified");
        } else if (!user.isVerified()) {
            response.setSuccess(false);
            response.addError("email",
                    "your account has not been approved by an admin yet.");
        } else if (!passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            response.setSuccess(false);
            response.addError("password", "Password is incorrect");
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
//              update last_login
                userRepository.updateLastLogin(user.getId());

                revokeAllUserTokens(user);
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, jwtToken);
                response.setSuccess(true);
                response.setAccessToken(jwtToken);
                response.setRefreshToken(refreshToken);
                response.setId(user.getId());
                response.setEmail(user.getEmail());
                response.setUserName(user.getUsername());
                response.setRole(user.getRole());
                response.setFirstName(user instanceof Candidate ? ((Candidate) user).getFirstName() : user instanceof Organization ? ((Organization) user).getFirstName() : ( user instanceof Admin ? ((Admin) user).getFirstName() : null ));
                response.setLastName(user instanceof Candidate ? ((Candidate) user).getLastName() : null);

            } catch (Exception e) {
                response.setSuccess(false);
                response.addError("auth", "Authentication failed");
            }
        }

        return response;
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(
                user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

}

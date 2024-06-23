package com.testify.Testify_Backend.service;

import com.testify.Testify_Backend.model.Attendee;
import com.testify.Testify_Backend.repository.AttendeeRepository;
import com.testify.Testify_Backend.requests.auth.AuthenticationRequest;
import com.testify.Testify_Backend.responses.auth.AuthenticationResponse;
import com.testify.Testify_Backend.requests.auth.RegistrationRequest;
import com.testify.Testify_Backend.responses.auth.RegisterResponse;
import com.testify.Testify_Backend.enums.TokenType;
import com.testify.Testify_Backend.enums.UserRole;
import com.testify.Testify_Backend.model.ConfirmationToken;
import com.testify.Testify_Backend.model.Token;
import com.testify.Testify_Backend.model.User;
import com.testify.Testify_Backend.repository.TokenRepository;
import com.testify.Testify_Backend.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDateTime;
import java.util.UUID;

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
    private final AttendeeRepository attendeeRepository;
    private User user;

    //    public AuthenticationResponse register(RegistrationRequest request) {
//        user = User.builder()
//                .email(request.getEmail())
//                .username(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(UserRole.EXAM_ATTENDEE)
//                .enabled(false)
//                .locked(false)
//                .build();
//
//        userRepository.save(user);
//        // TODO: Send confirmation token
//        String token = UUID.randomUUID().toString();
//
//        ConfirmationToken confirmationToken = new ConfirmationToken(
//                token,
//                LocalDateTime.now(),
//                LocalDateTime.now().plusMinutes(15),
//                user
//        );
//        confirmationTokenService.saveConfirmationToken(confirmationToken);
//
//        // TODO: Send email
//        String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;
//        emailSender.send(
//                request.getEmail(),
//                buildEmail(request.getEmail(), link)
//        );
//
//
//        var jwtToken = jwtService.generateToken(user);
//
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//
//
//    }
    public RegisterResponse register(@ModelAttribute RegistrationRequest request, boolean preVerified) {

        var response = new RegisterResponse();

        if (userService.findByEmail(request.getEmail()).isPresent()) {
            response.addError("email", "Email is already taken");
        }

        if (response.checkValidity(request)) {
//
            User savedUser = null;
            if (request.getRole().equals(UserRole.ATTENDEE)) {
                Attendee attendee = Attendee.builder()
                        .email(request.getEmail())
                        .username(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .bio(request.getBio())
                        .contactNo(request.getContactNo())
                        .role(UserRole.ATTENDEE)
                        .enabled(false)
                        .locked(false)
                        .verified(true)
                        .build();
                savedUser = attendeeRepository.save(attendee);
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
            emailSender.send(
                    request.getEmail(),
                    buildEmail(request.getEmail(), link)
            );

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
                .tokenType(TokenType.BEARER).revoked(false)
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
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "        \n" +
                "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "          <tbody><tr>\n" +
                "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td style=\"padding-left:10px\">\n" +
                "                  \n" +
                "                    </td>\n" +
                "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "              </a>\n" +
                "            </td>\n" +
                "          </tr>\n" +
                "        </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "      <td>\n" +
                "        \n" +
                "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                  <tbody><tr>\n" +
                "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                "                  </tr>\n" +
                "                </tbody></table>\n" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "    </tr>\n" +
                "  </tbody></table>\n" +
                "\n" +
                "\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "        \n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                "        \n" +
                "      </td>\n" +
                "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "      <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                "\n" +
                "</div></div>";
    }

//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        request.getEmail(),
//                        request.getPassword()
//                )
//        );
//
//        var user = userRepository.findByEmail(request.getEmail())
//                .orElseThrow();
//
//        // Check if the user is enabled
//        if (!user.isEnabled()) {
//            throw new IllegalStateException("User is not enabled");
//        }
//
//
//        var jwtToken = jwtService.generateToken(user);
//
//        return AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//
//    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        var response = new AuthenticationResponse();
        var user = userService.findByEmail(request.getEmail()).orElse(null);

        if (user == null) {
            response.addError("email", "Email does not exist");
        } else if (!user.isEnabled()) {
            response.addError("email", "Email is not verified");
        } else if (!user.isVerified()) {
            response.addError("email",
                    "your account has not been approved by an admin yet.");
        } else if (!passwordEncoder.matches(request.getPassword(),
                user.getPassword())) {
            response.addError("password", "Password is incorrect");
        } else {
            try {
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
                var jwtToken = jwtService.generateToken(user);
                var refreshToken = jwtService.generateRefreshToken(user);
                saveUserToken(user, jwtToken);
                response.setSuccess(true);
                response.setAccessToken(jwtToken);
                response.setRefreshToken(refreshToken);
                response.setId(user.getId());
                response.setEmail(user.getEmail());
                response.setRole(user.getRole());
            } catch (Exception e) {
                response.setSuccess(false);
                response.addError("auth", "Authentication failed");
            }
        }

        return response;
    }

}

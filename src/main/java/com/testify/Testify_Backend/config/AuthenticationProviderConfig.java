package com.testify.Testify_Backend.config;

import com.testify.Testify_Backend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

@Configuration
@RequiredArgsConstructor
public class AuthenticationProviderConfig {
    private final UserService userService;
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.loadUserByUsername(username));
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}

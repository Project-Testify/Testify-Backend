package com.testify.Testify_Backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@NonNull CorsRegistry registry) { // Explicitly mark parameter as non-null
                registry.addMapping("/**") // You can specify more granular paths instead of /**
                        .allowedOrigins("http://127.0.0.1:4500/", "http://localhost:4500") // URL of the React app
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH") // Allowed HTTP methods
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}
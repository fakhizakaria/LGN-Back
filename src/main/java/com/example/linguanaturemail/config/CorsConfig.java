package com.example.linguanaturemail.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/contact/submit")
            .allowedOrigins("http://localhost:4200") // Replace with your Angular app URL
            .allowedMethods("GET", "POST") // Add allowed HTTP methods
            .allowedHeaders("*") // Allow all headers (you can configure specific headers)
            .allowCredentials(true); // Allow credentials (if needed)
    }
}


package com.example.backend;

import com.example.backend.simulatorIntegration.configuration.SharedFolderConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {
    @Bean
    public SharedFolderConfig sharedFolderConfig(){
        return new SharedFolderConfig();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("http://localhost:8080","http://dronhub.pl","http://dronhub.pl:8080","http://localhost");
            }
        };
    }
}

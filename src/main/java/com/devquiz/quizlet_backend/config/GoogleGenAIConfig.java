package com.devquiz.quizlet_backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@ConfigurationProperties(prefix ="spring.google.genai")
public class GoogleGenAIConfig {
    private String apiKey;
    private String modelName;
    private String timeout;

}

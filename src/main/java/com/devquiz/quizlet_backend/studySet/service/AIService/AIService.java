package com.devquiz.quizlet_backend.studySet.service.AIService;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpHeaders;

import jakarta.annotation.PostConstruct;import java.util.List;
import java.util.Map;
//import java.net.http.HttpHeaders;

@Service
public class AIService {
    @Value("${spring.google.genai.api-key}")
    private String apiKey;

    @Value("${spring.google.genai.model-name}") // Thêm giá trị mặc định nếu cần
    private String modelName;

    private Client client;

    // Sử dụng PostConstruct để khởi tạo client sau khi apiKey đã được nạp
    @PostConstruct
    public void init() {
        System.out.println("Initializing AIService with API Key: " + apiKey);
//        System.out.println("Using model: " + modelName);
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }    public String generateDescription (String title) {
         GenerateContentResponse response = client.models.generateContent(modelName, "Write a description for a study set with the title: " + title + " less than 50 words",null);
            return response.text();
    }

}

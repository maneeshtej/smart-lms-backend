package com.lms.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

@Service
public class RagService {

    @Value("${rag.server.url}")
    private String ragServerUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String sendToRag(File file, Long courseId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(file));
            body.add("courseId", courseId.toString());

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);
            ResponseEntity<String> response =
                    restTemplate.postForEntity(ragServerUrl, request, String.class);

            return response.getBody();

        } catch (Exception e) {
            e.printStackTrace();
            return "{\"status\":\"error\",\"message\":\"Failed to call Flask RAG: " + e.getMessage() + "\"}";
        }
    }
}

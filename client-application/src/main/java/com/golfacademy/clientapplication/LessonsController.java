package com.golfacademy.clientapplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;

import static org.springframework.security.oauth2.client.web.client.RequestAttributeClientRegistrationIdResolver.clientRegistrationId;

@RestController
@RequiredArgsConstructor
public class LessonsController {

    private final RestClient restClient;

    @GetMapping("/lessons")
    public String fetchLessons() {
        return restClient.get()
                .uri("http://localhost:8081/lessons")
                .attributes(clientRegistrationId("golf-client"))
                .retrieve()
                .body(String.class);
    }


}

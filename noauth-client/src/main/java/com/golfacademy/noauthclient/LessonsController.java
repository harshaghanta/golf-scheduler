package com.golfacademy.noauthclient;

import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchProperties.Restclient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class LessonsController {
    private final RestClient restClient;

    public LessonsController(RestClient.Builder builder, LogRequestResponseInterceptor logRequestResponseInterceptor) {
        this.restClient = builder.baseUrl("http://localhost:8081")
                .requestInterceptor(logRequestResponseInterceptor)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (request, response) -> {
                    if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                                "Unauthorized access to lessons API");
                    }
                    throw new ResponseStatusException(response.getStatusCode(), "Client error occured");
                })
                .build();
    }
    @GetMapping("/lessons")
    public String fetchLessons() {
        return restClient.get().uri("/lessons").retrieve().body(String.class);
    }

    @GetMapping("/posts")
    public String fetchPosts(LogRequestResponseInterceptor logRequestResponseInterceptor) {
        RestClient client = RestClient.builder().baseUrl("https://jsonplaceholder.typicode.com/")
        .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .requestInterceptor(logRequestResponseInterceptor)
        .build();
        return client.get().uri("/posts").retrieve().body(String.class);
    }

}

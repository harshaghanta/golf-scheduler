package com.golfacademy.noauthclient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogRequestResponseInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        log.info("Received Request for url: {} with headers: {}", request.getURI(), request.getHeaders());
        ClientHttpResponse response = execution.execute(request, body);
        byte[] resposeBytes = response.getBody().readAllBytes();
        log.info("Received response with status code: {} and headers: {} and body: {}", response.getStatusCode(),
                response.getHeaders(), new String(resposeBytes, "UTF-8"));

        //Response once read can't be read again so we need to create a new response object
        
        // Create a new response object with the same status code and headers but with
        // the body created from the response bytes

        return new BufferingClientHttpResponseWrapper(response, resposeBytes);
    }

    private static class BufferingClientHttpResponseWrapper implements ClientHttpResponse {

        private ClientHttpResponse response;
        private byte[] body;

        public BufferingClientHttpResponseWrapper(ClientHttpResponse response, byte[] body) {
            this.response = response;
            this.body = body;
        }

        @Override
        public InputStream getBody() throws IOException {
            return new ByteArrayInputStream(body);
        }

        @Override
        public HttpHeaders getHeaders() {
            return response.getHeaders();
        }

        @Override
        public HttpStatusCode getStatusCode() throws IOException {
            return response.getStatusCode();
        }

        @Override
        public String getStatusText() throws IOException {
            return response.getStatusText();
        }

        @Override
        public void close() {
            response.close();
        }

    }

}

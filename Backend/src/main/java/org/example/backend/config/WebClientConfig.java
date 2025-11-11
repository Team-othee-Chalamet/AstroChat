package org.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient.Builder WebClientBuilder() {
        return WebClient.builder()
             // Mistral sends json as default answer, so .ACCEPT should be abundant
             // .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    }

    //Register the bean with spring
    @Bean   //Create a WebClient for API contact
    public WebClient MistralClientBuilder(WebClient.Builder builder) {
        //Return a WebClient using the builder method in webclient
        System.out.println(System.getenv("MISTRAL_API_KEY"));
        return builder.clone()
                //The base URL for the Mistral API contact
                .baseUrl("https://api.mistral.ai")
                //Create one header, bearing the Auth token
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + System.getenv("MISTRAL_API_KEY"))
                .build();
    }

    @Bean
    public WebClient AztroClient(WebClient.Builder builder) {
        return builder.clone()
                .baseUrl("https://aztro.sameerkumar.website")
                .build();
    }
}

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
        return builder.clone()
                //The base URL for the Mistral API contact
                .baseUrl("https://api.mistral.ai")
                //Create one header, bearing the Auth token
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + System.getenv("MISTRAL_API_KEY"))
                .build();
    }//JSON request format
    /*
    {
  "model": "mistral-large-latest",
  "messages": [
    {
      "role": "user",
      "content": "Return the name and ingredients of a French meal in a JSON object."
    }
  ],
  "response_format": {
    "type": "json_object"
  }
}
*/

    @Bean
    public WebClient HoroscopeAPIVerveClientBuilder(WebClient.Builder builder) {
        return builder.clone()
                .baseUrl("https://api.apiverve.com/v1")
                .defaultHeader("X-API-Key", System.getenv("HOROSCOPE_API_KEY"))
                .build();
    }
}

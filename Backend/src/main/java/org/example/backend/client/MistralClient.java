package org.example.backend.client;

import org.example.backend.records.ChatMessage;
import org.example.backend.records.ChatRequest;
import org.example.backend.records.ChatResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MistralClient {
    private final WebClient mistralClient;

    public MistralClient(@Qualifier("MistralClientBuilder") WebClient mistralClient){
        this.mistralClient = mistralClient;
    }

    public Mono<ChatResponse> getChatResponse(List<ChatMessage> messages) {
        // The request should be based on logic from the service layer
        ChatRequest request = new ChatRequest("mistral-small-latest",
                messages);
        return mistralClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ChatResponse.class);
    }

}

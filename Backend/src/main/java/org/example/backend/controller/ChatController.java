package org.example.backend.controller;

import org.example.backend.records.ChatMessage;
import org.example.backend.records.ChatResponse;
import org.example.backend.records.Choice;
import org.example.backend.service.MistralService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/astrochat")
public class ChatController {
    private final MistralService mistralService;

    public ChatController(MistralService mistralService){
        this.mistralService = mistralService;
    }

    @PostMapping
    public Mono<ChatResponse> chatResponseMono(@RequestBody String userMessage){
        return mistralService.sendAndRecieveMessage("1", new ChatMessage("user", userMessage));
    }

    @PostMapping("/test")
    public Mono<ChatResponse> testChatResponse(String userMessage){
        ChatResponse resp = new ChatResponse("IDTEST", List.of(new Choice(new ChatMessage("assistant", "Recieved message content"))));
        return Mono.just(resp);
    }


}

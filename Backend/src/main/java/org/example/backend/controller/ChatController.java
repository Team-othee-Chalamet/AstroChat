package org.example.backend.controller;

import org.example.backend.records.ChatMessage;
import org.example.backend.records.ChatResponse;
import org.example.backend.records.Choice;
import org.example.backend.records.Horoscope;
import org.example.backend.service.ChatService;
import org.example.backend.service.MistralService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/astrochat")
public class ChatController {
    private final MistralService mistralService;
    private final ChatService chatService;

    public ChatController(MistralService mistralService, ChatService chatService){
        this.mistralService = mistralService;
        this.chatService = chatService;
    }

    @PostMapping
    public Mono<ChatResponse> subsequentChatResponseMono(@RequestParam String userMessage,
                                                         @RequestParam String sessionId){
        return mistralService.sendAndReceiveMessage(sessionId, new ChatMessage("user", userMessage));
    }

    @PostMapping("/send")
    public Mono<ChatResponse> firstChatResponseMono(@RequestParam String name,
                                                    @RequestParam String date,
                                                    @RequestParam String userMessage,
                                                    @RequestParam String sessionId){
     // Vi skal finde stjernetegnet
        LocalDate birthday = LocalDate.parse(date);
        String sign = String.valueOf(chatService.getHoroscope(birthday));
     // Vi skal sende en firstmessage sammen med vores prompt
        ChatMessage firstChatMessage = chatService.setFirstChatMessage(name, birthday, userMessage);
     // Vi skal returnere chat responset
        return mistralService.sendAndReceiveMessage(sessionId,firstChatMessage);
    }


    @PostMapping("/test")
    public Mono<ChatResponse> testChatResponse(String userMessage){
        ChatResponse resp = new ChatResponse("IDTEST", List.of(new Choice(new ChatMessage("assistant", "Recieved message content"))));
        return Mono.just(resp);
    }

    @GetMapping("/horoscope")
    public Mono<Horoscope> getHoroscope(@RequestBody LocalDate birthdate) {
        return chatService.getHoroscope(birthdate);
    }

}

package org.example.backend.service;

import org.example.backend.client.MistralClient;
import org.example.backend.records.ChatMessage;
import org.example.backend.records.ChatResponse;
import org.example.backend.repository.ChatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class MistralService {
    MistralClient mistralClient;

    public MistralService(MistralClient mistralClient){
        this.mistralClient = mistralClient;
    }


    //Send and recieve a message
    public Mono<ChatResponse> sendAndRecieveMessage(String id, ChatMessage message){
        List<ChatMessage> messages = ChatService.saveUserMessage(id, message);
        //Send the complete list to mistral
        return mistralClient.getChatResponse(messages);
        //return ChatRepository.saveMessage(id, mistralClient.getChatResponse(messages));
    }
}

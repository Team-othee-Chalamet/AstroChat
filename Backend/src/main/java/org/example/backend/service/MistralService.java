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
public Mono<ChatResponse> sendAndReceiveMessage(String id, ChatMessage message) {
    List<ChatMessage> messages = ChatService.saveUserMessage(id, message);
    System.out.println(messages);
    return mistralClient.getChatResponse(messages)
        .doOnNext(chatResponse -> {
            ChatMessage mistralResponse = chatResponse.choices().getFirst().message();
            messages.add(mistralResponse);
            ChatRepository.chatHistories.put(id, messages);
        });
}

    //Send and recieve a message
/*    public Mono<ChatResponse> sendAndReceiveMessage(String id, ChatMessage message){

        // Save usermessage
        List<ChatMessage> messages = ChatService.saveUserMessage(id, message);

        System.out.println(messages);
        // Retrieve chats answer to the full chat
        Mono<ChatResponse> mistralResponseFullChat = mistralClient.getChatResponse(messages);

        // Retrieve chats answer detached from the full chat
        ChatMessage mistralResponse = mistralResponseFullChat.block().choices().getFirst().message();

        // Save the detached answer by attaching to
        messages.add(mistralResponse);

        //Send the complete list to mistral
        System.out.println("Det her er listen af messages i sendAdnReceiveMessage() " + messages);
        //ChatRepository.chatHistories.put(id, messages);
        return mistralResponseFullChat;
        // return ChatRepository.saveMessage(id, mistralClient.getChatResponse(messages));
    }*/
}

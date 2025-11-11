package org.example.backend.repository;

import jakarta.servlet.http.HttpSession;
import org.example.backend.records.ChatMessage;
import org.example.backend.records.ChatResponse;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;

@Repository
public class ChatRepository {
    HttpSession httpSession;

    public static HashMap<String, List<ChatMessage>> chatHistories = new HashMap<>();

    public static List<ChatMessage> getChatMessagesFromID(String id){
        return chatHistories.get(id);
    }

    public static Mono<ChatResponse> saveMessage(String id, Mono<ChatResponse> chatResponse){
        List<ChatMessage> messages = chatHistories.get(id);
        messages.add(chatResponse.block().choices().getFirst().message());
        chatHistories.put(id, messages);

        return chatResponse;
    }

}

package org.example.backend.repository;

import jakarta.servlet.http.HttpSession;
import org.example.backend.records.ChatMessage;

import java.util.HashMap;
import java.util.List;

public class ChatRepository {
    HttpSession httpSession;

    private static HashMap<String, List<ChatMessage>> chatHistories = new HashMap<>();

    public static List<ChatMessage> getChatMessagesFromID(String id){
        return chatHistories.get(id);
    }

    public static void saveMessage(String id, ChatMessage message){
        List<ChatMessage> messages = chatHistories.get(id);
        messages.add(message);
        chatHistories.put(id, messages);
    }
}

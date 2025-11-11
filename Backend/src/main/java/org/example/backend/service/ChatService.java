package org.example.backend.service;

import org.example.backend.records.ChatMessage;
import org.example.backend.repository.ChatRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {

    public static List<ChatMessage> saveUserMessage(String id, ChatMessage message){
        List<ChatMessage> userMessages = ChatRepository.getChatMessagesFromID(id);

        //Check if this is the first message this user has sent
        if (userMessages == null){
            userMessages = new ArrayList<>();

            //Add the first message to the chain
            userMessages.add(setFirstChatMessage(/* Here we put the form data with name etc*/));

            userMessages.add(message);
        }
        else {
            userMessages.add(message);
        }

        ChatRepository.chatHistories.put(id, userMessages);

        return userMessages;
    }

    static ChatMessage setFirstChatMessage(/*Form data*/){
        return new ChatMessage("system", "Hello, act in this chat as mystical fortune teller. Do not make replies too long, and really go wild with the" +
                " strange mystical talking patterns. Remain vague and bizzare. Here is the users following question:");
    }
}

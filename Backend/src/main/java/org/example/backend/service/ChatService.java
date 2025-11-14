package org.example.backend.service;

import org.example.backend.client.HoroscopeClient;
import org.example.backend.records.ChatMessage;
import org.example.backend.records.Horoscope;
import org.example.backend.repository.ChatRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ChatService {
    private final HoroscopeClient horoscopeClient;

    public ChatService(HoroscopeClient horoscopeClient){
        this.horoscopeClient = horoscopeClient;
    }

    public static List<ChatMessage> saveUserMessage(String id, ChatMessage message){
        List<ChatMessage> userMessages = ChatRepository.getChatMessagesFromID(id);

        //Check if this is the first message this user has sent
        if (userMessages == null){
            userMessages = new ArrayList<>();

            //Add the first message to the chain
         //   userMessages.add(setFirstChatMessage(/* Here we put the form data with name etc*/));
            userMessages.add(message);
        }
        else {
            userMessages.add(message);
        }

        ChatRepository.chatHistories.put(id, userMessages);

        return userMessages;
    }

    public ChatMessage setFirstChatMessage(/*Form data*/String name, LocalDate birthDay, String firstMessage){
        //Sætter sign fra den dato man modtager
        String sign = getSignFromBirthday(birthDay);
        // Laver et horoscope opbjekt fra api'en bruger .block fordi den er asynkron
        Horoscope horoscope = horoscopeClient.getHoroscope(sign).block();
        //Sætter horoscop teksten så den kan bruges i den første besked
        String horoscopeText = horoscope.horoscope();
        return new ChatMessage("system", "Hello, act in this chat as mystical fortune teller. Make replies brief, 100 words maximum but keep the" +
                " strange mystical talking patterns. Remain vague and bizzare."+ "Base your response on the text from the persons horoscope: "
                + horoscopeText + " Their name is: "+name + " Here is the users question: " + firstMessage + ". Answer in the same language that they use.");
    }

    public Mono<Horoscope> getHoroscope(LocalDate birthDate) {
        String sign = getSignFromBirthday(birthDate);
        return horoscopeClient.getHoroscope(sign);
    }

    public String getSignFromBirthday(LocalDate birthDate) {
        int month = birthDate.getMonthValue();
        int day = birthDate.getDayOfMonth();

        switch (month) {
            case 1:
                return (day < 20) ? "capricorn" : "aquarius";
            case 2:
                return (day < 19) ? "aquarius" : "pisces";
            case 3:
                return (day < 21) ? "pisces" : "aries";
            case 4:
                return (day < 20) ? "aries" : "taurus";
            case 5:
                return (day < 21) ? "taurus" : "gemini";
            case 6:
                return (day < 21) ? "gemini" : "cancer";
            case 7:
                return (day < 23) ? "cancer" : "leo";
            case 8:
                return (day < 23) ? "leo" : "virgo";
            case 9:
                return (day < 23) ? "virgo" : "libra";
            case 10:
                return (day < 23) ? "libra" : "scorpio";
            case 11:
                return (day < 22) ? "scorpio" : "sagittarius";
            case 12:
                return (day < 22) ? "sagittarius" : "capricorn";
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }
}
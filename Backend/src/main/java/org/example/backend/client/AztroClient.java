package org.example.backend.client;

import org.example.backend.records.Horoscope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AztroClient {
    private final WebClient aztroClient;

    public AztroClient(@Qualifier("AztroClient") WebClient aztroClient) {
        this.aztroClient = aztroClient;
    }

//    public Mono<Horoscope> getHoroscope(String sign, String day){
//        return aztroClient.post()
//                .bodyValue("").retrieve();
//    }
}

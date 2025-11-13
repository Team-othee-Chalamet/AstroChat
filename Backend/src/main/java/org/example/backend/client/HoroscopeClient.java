package org.example.backend.client;

import org.example.backend.records.Horoscope;
import org.example.backend.records.HoroscopeResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HoroscopeClient {
    private final WebClient horoscopeClient;

    public HoroscopeClient(@Qualifier("HoroscopeAPIVerveClientBuilder") WebClient horoscopeClient){
        this.horoscopeClient = horoscopeClient;
    }

    public Mono<Horoscope> getHoroscope(String sign){
        return horoscopeClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/horoscope")
                        .queryParam("sign", sign)
                        .build())
                .retrieve()
                .bodyToMono(HoroscopeResponse.class)
                .map(HoroscopeResponse::data);
    }
}

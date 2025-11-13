package org.example.backend.client;

import com.fasterxml.jackson.databind.JsonNode;
import org.example.backend.records.Horoscope;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class HoroscopeAPIVerveClient {
    private final WebClient horoscopeClient;

    public HoroscopeAPIVerveClient(@Qualifier("HoroscopeAPIVerveClientBuilder") WebClient horoscopeClient){
        this.horoscopeClient = horoscopeClient;
    }

    public Mono<Horoscope> getHoroscope(String sign){
        return horoscopeClient.post()
                .bodyValue(sign)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(json -> {
                    JsonNode data = json.path("data");
                    return new Horoscope(
                            data.path("sign").asText(),
                            data.path("horoscope").asText()
                    );
                });
    }
}

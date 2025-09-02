package br.com.dourado.pagamento.simplificado.api.infra.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class WebClientHelper {

    @Autowired
    private WebClient webClient;

    public <T> Mono<T> get(String url, Class<T> responseType) {
        return webClient.get()
                .uri(url)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T, R> Mono<T> post(String url, R requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    public <T, R> Mono<T> put(String url, R requestBody, Class<T> responseType) {
        return webClient.put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(responseType);
    }

    public Mono<Void> delete(String url) {
        return webClient.delete()
                .uri(url)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

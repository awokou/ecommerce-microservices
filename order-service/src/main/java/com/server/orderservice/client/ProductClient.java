package com.server.orderservice.client;

import com.server.orderservice.config.ServiceUrlConfig;
import com.server.orderservice.domain.dto.request.PurchaseRequest;
import com.server.orderservice.domain.dto.response.PurchaseResponse;
import com.server.orderservice.exception.OrderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductClient {

    private final WebClient webClient;
    private final ServiceUrlConfig config;


    public Mono<List<PurchaseResponse>> purchaseProducts(List<PurchaseRequest> requestBody) {

        return webClient.post()
                .uri(config.getProduct().getUrl() + "/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .map(body -> new OrderNotFoundException(
                                        "Error while processing product purchase :: " + body
                                ))
                )
                .bodyToMono(new ParameterizedTypeReference<>() {});
    }
}

package com.caching.proxy.handler;

import com.caching.proxy.service.ProxyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProxyHandler {

    private final ProxyService proxyService;

    public Mono<ServerResponse> handleProxy(ServerRequest request) {
        return proxyService.handleRequest(request.exchange())
                .flatMap(cachedResponse -> ServerResponse
                        .status(HttpStatusCode.valueOf(cachedResponse.getStatusCodeValue()))
                        .headers(headers -> headers.addAll(cachedResponse.getHeaders()))
                        .bodyValue(cachedResponse.getBody()));
    }
}

package com.caching.proxy.configuration;

import com.caching.proxy.handler.ProxyHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Component
@RequiredArgsConstructor
public class RouterConfig {

    @Value("${proxy.origin}")
    private String originUrl;

    @Bean
    public RouterFunction<ServerResponse> proxyRouter(ProxyHandler proxyHandler) {
        return RouterFunctions.route()
                .GET("/**", proxyHandler::handleProxy)
                .filter((request, next) -> {
                    request.exchange().getAttributes().put("originUrl", originUrl);
                    return next.handle(request);
                })
                .build();
    }

}

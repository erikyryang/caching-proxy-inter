package com.caching.proxy.service;
import com.caching.proxy.model.CachedResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Service
public class ProxyService {
    private static final Logger logger = LoggerFactory.getLogger(ProxyService.class);

    private final WebClient webClient;
    private final Cache cache;

    public ProxyService(WebClient.Builder webClientBuilder, CacheManager cacheManager) {
        this.webClient = webClientBuilder.build();
        this.cache = cacheManager.getCache("proxy-cache");
    }

    public Mono<CachedResponse> handleRequest(ServerWebExchange exchange) {
        String cacheKey = generateCacheKey(exchange);

        return Mono.justOrEmpty(cache.get(cacheKey, CachedResponse.class))
                .map(cachedResponse -> {
                    logger.info("CACHE HIT para a chave: {}", cacheKey);
                    HttpHeaders mutableHeaders = new HttpHeaders();
                    mutableHeaders.addAll(cachedResponse.getHeaders());
                    mutableHeaders.add("X-Cache", "HIT");

                    return new CachedResponse(
                            cachedResponse.getBody(),
                            mutableHeaders,
                            cachedResponse.getStatusCodeValue()
                    );
                })
                .switchIfEmpty(Mono.defer(() -> fetchFromOrigin(exchange, cacheKey)));
    }

    private Mono<CachedResponse> fetchFromOrigin(ServerWebExchange exchange, String cacheKey) {
        String originUrl = (String) exchange.getAttributes().get("originUrl");
        String path = exchange.getRequest().getURI().getPath();
        String query = exchange.getRequest().getURI().getQuery();
        String fullUrl = originUrl + path + (query != null ? "?" + query : "");

        logger.info("CACHE MISS para a chave: {}. A aceder à origem: {}", cacheKey, fullUrl);
        return webClient.get()
                .uri(fullUrl)
                .headers(headers -> {
                    // used to create a fake user agent
                    headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                })
                .exchangeToMono(this::processOriginResponse)
                .doOnNext(response -> {
                    cache.put(cacheKey, response);
                });
    }

    private Mono<CachedResponse> processOriginResponse(ClientResponse clientResponse) {
        return clientResponse.bodyToMono(byte[].class)
                .defaultIfEmpty(new byte[0])
                .map(body -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.addAll(clientResponse.headers().asHttpHeaders());
                    headers.add("X-Cache", "MISS");

                    return new CachedResponse(
                            body,
                            headers,
                            clientResponse.statusCode().value()
                    );
                });
    }

    private String generateCacheKey(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String query = exchange.getRequest().getURI().getQuery();
        return path + (query != null ? "?" + query : "");
    }
}
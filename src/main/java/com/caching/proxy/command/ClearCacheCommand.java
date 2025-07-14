package com.caching.proxy.command;


import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.Callable;

@Command(name = "--clear-cache", description = "Clears the proxy cache.")
public class ClearCacheCommand implements Callable<Integer> {

    @Option(names = "--port", defaultValue = "8080", description = "Port of the running proxy server.")
    private int port;

    @Override
    public Integer call() throws Exception {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:" + port + "/_internal/cache/clear"))
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                System.out.println("✅ Cache cleared successfully.");
                return 0;
            } else {
                System.err.println("❌ Failed to clear cache. Server responded with status: " + response.statusCode());
                return 1;
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to connect to the proxy server on port " + port + ". Is it running?");
            return 1;
        }
    }
}

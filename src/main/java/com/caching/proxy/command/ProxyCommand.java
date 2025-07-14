package com.caching.proxy.command;

import com.caching.proxy.CachingProxyApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "caching-proxy", mixinStandardHelpOptions = true,
        description = "Starts the caching proxy server.")
public class ProxyCommand implements Callable<Integer> {

    @Option(names = "--port", required = true, description = "The port for the proxy server.")
    private int port;

    @Option(names = "--origin", required = true, description = "The origin server URL.")
    private String origin;

    @Override
    public Integer call() throws Exception {
        System.setProperty("server.port", String.valueOf(port));
        System.setProperty("proxy.origin", origin);

        SpringApplication.run(CachingProxyApplication.class, new String[0]);

        Thread.currentThread().join();

        return 0;
    }
}
package com.caching.proxy;

import com.caching.proxy.command.ClearCacheCommand;
import com.caching.proxy.command.ProxyCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import picocli.CommandLine;
import picocli.CommandLine.IFactory;

@EnableCaching
@SpringBootApplication
public class CachingProxyApplication {

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new ProxyCommand());
		commandLine.addSubcommand(new ClearCacheCommand());

		System.exit(commandLine.execute(args));
	}

}

package com.caching.proxy;

import com.caching.proxy.command.ProxyCommand;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import picocli.CommandLine;

@EnableCaching
@SpringBootApplication
public class CachingProxyApplication {

	public static void main(String[] args) {
		CommandLine commandLine = new CommandLine(new ProxyCommand());
		System.exit(commandLine.execute(args));
	}

}

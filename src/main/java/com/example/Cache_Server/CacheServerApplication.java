package com.example.Cache_Server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class CacheServerApplication {

	public static void main(String[] args) {

		Map<String,Object> properties = new HashMap<>();
		boolean clearCacheMode=false;
		for (int i = 0; i < args.length; i++) {
			String arg = args[i];

			// ---port=3000
			if (arg.startsWith("--port=")) {
				properties.put("server.port", arg.substring("--port=".length()));
			}

			if ("--clear-cache".equals(arg)) {
				clearCacheMode = true;
			}
			if (arg.startsWith("--target.port=")) {
				properties.put("target.port", arg.substring("--target.port=".length()));
			}

			// ---origin.url=https://...
			if (arg.startsWith("--origin.url=")) {
				properties.put("origin.url", arg.substring("--origin.url=".length()));
			}
		}
		SpringApplication app = new SpringApplication(CacheServerApplication.class);

		if (clearCacheMode) {
			app.setWebApplicationType(WebApplicationType.NONE);
		}

		if(!properties.isEmpty()){
			app.setDefaultProperties(properties);
		}
		ConfigurableApplicationContext context =  app.run(args);

		String port = context.getEnvironment().getProperty("server.port");
		String origin = context.getEnvironment().getProperty("origin.url");

		System.out.println("🚀 Cache Server started on port: " + port);
		System.out.println("🌍 Proxying requests to origin: " + origin);
	}

}

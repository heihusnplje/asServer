package com.tigerjoys.onion.pcserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class Bootstrap {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Bootstrap.class, args);
		String[] activeProfiles = ctx.getEnvironment().getActiveProfiles();
		for (String profile : activeProfiles) {
			LOGGER.warn("Spring Boot 使用profile为:{}", profile);
		}
	}
	
}

package com.blackdragon.heytossme.component;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

	private static final int MAX_AGE = 1000* 60 * 60;
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		WebMvcConfigurer.super.addCorsMappings(registry);
		registry.addMapping("/**")
				.allowedOrigins("*")	//일단 와일드카드로 설정
				.allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
				.allowedHeaders("*")
				.allowCredentials(true)
				.maxAge(MAX_AGE);
	}
}

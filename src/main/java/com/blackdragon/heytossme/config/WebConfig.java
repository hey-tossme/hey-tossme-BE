package com.blackdragon.heytossme.config;

import com.blackdragon.heytossme.interceptor.TokenInterceptor;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.InputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final int MAX_AGE = 1000 * 60 * 60;
    private final TokenInterceptor tokenInterceptor;
    @Value("${com.blackdragon.fcm.path}")
    private String fcmPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .order(1)
                .addPathPatterns("/**/v1/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        WebMvcConfigurer.super.addCorsMappings(registry);
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(MAX_AGE);
    }

    @Bean
    public FirebaseApp initializer() {
        try {
            log.info("initialized start");
            InputStream inputStream =
                    new ClassPathResource("jar:file:/" + fcmPath).getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .setProjectId("heytossme2")
                    .build();
            log.info("initialized completed");
            return FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            log.error("error message = {}", e.getMessage());
        }
        return null;
    }
}

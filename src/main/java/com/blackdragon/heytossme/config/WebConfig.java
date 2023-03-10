package com.blackdragon.heytossme.config;

import com.blackdragon.heytossme.interceptor.TokenInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final TokenInterceptor bookmarkInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info(">>>>>>>>>>> 인터셉터 구현 >>>>>>>>>>");
        registry.addInterceptor(bookmarkInterceptor)
                .order(1)
                .addPathPatterns("/bookmarks");
    }
}

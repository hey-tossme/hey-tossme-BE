package com.blackdragon.heytossme.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws") // ws://localhost:8080/ws 로 요청 보낼시 STOMP 생성
                .setAllowedOriginPatterns("*");
//                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry.setPathMatcher(new AntPathMatcher(".")); //url -> chat/room/3 -> chat.room.3 왜 쓰지?
//        registry.setApplicationDestinationPrefixes("/pub"); // 매세지를 발행
//
//        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange",
//                "/amq/queue"); // 어떤 채팅방에 입장할 것인지 (구독)
    }
}

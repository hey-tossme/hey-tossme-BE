package com.blackdragon.heytossme.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${com.blackdragon.rabbitmq.clientId}")
    private String clientId;
    @Value("${com.blackdragon.rabbitmq.clientPw}")
    private String clientPw;
    @Value("${com.blackdragon.rabbitmq.virtualHost}")
    private String virtualHost;
    @Value("${com.blackdragon.rabbitmq.host}")
    private String host;
    @Value("${com.blackdragon.rabbitmq.port}")
    private int port;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {

        registry.addEndpoint("/ws") // ws://localhost:8080/ws 로 요청 보낼시 STOMP 생성
                .setAllowedOriginPatterns("*");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(3).maxPoolSize(5);
    }

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(3).maxPoolSize(5);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setPathMatcher(new AntPathMatcher(".")); //url -> chat/room/3 -> chat.room.3 왜 쓰지?
        registry.setApplicationDestinationPrefixes("/pub"); // 매세지를 발행 /pub/room.1.message

        registry.enableStompBrokerRelay("/queue", "/topic",
                        "/exchange", "/amq/queue")
                .setRelayHost(host)
                .setRelayPort(port)
                .setClientLogin(clientId)
                .setClientPasscode(clientPw)
                .setVirtualHost(virtualHost); // 어떤 채팅방에 입장할 것인지 (구독)
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }
}

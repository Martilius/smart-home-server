package com.smarthome.server.dao;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;


@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class ConfigurationWebSocketDao implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/mywebsocket").setAllowedOrigins("*").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/rooms","/device","/room", "/user", "/scenery");
        registry.setApplicationDestinationPrefixes("/app","/rooms","/device", "/user", "/scenery");
    }

}

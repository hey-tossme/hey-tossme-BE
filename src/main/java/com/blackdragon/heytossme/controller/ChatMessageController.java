package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.persist.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    public void getMessageList(Long roomId, @RequestHeader("Authorization") String token) {

        return;
    }

    @MessageMapping("/{roomId}")
    @SendTo("/room/{roomId}")
    public void sendMessage(@DestinationVariable Long roomId, ChatMessage message) {

        return;
    }
}

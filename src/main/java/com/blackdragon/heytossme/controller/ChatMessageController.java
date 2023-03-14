package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MessageDto;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatMessageController {

    private static final String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final ChatService chatService;
    private final RabbitTemplate template;

    @GetMapping("/chat/message/{roomId}")
    public ResponseEntity<ResponseForm> getMessageList(@PathVariable Long roomId) {
        var data = chatService.getChatRoomMessage(roomId);

        return ResponseEntity.ok(new ResponseForm("ok", data));
    }

    @MessageMapping("chat.message.{chatRoomId}")
    public void sendMessage(@Payload MessageDto.SendMessage request,
            @DestinationVariable String chatRoomId) {
        log.info("message = {}", request.toString());
        chatService.sendMessage(request);
        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, request);
    }
}

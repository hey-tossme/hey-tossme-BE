package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MessageDto;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import jakarta.servlet.http.HttpServletRequest;
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
    private final String USER_ID = "userId";

    @GetMapping("/chat/message/{roomId}")
    public ResponseEntity<ResponseForm> getMessageList(HttpServletRequest httpRequest,
            @PathVariable Long roomId) {
        Long userId = (Long) httpRequest.getAttribute(USER_ID);
        var data = chatService.getChatRoomMessage(roomId, userId);

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

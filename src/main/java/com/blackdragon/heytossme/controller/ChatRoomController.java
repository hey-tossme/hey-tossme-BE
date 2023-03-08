package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import com.blackdragon.heytossme.type.ChatRoomType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chatrooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<ResponseForm> getChatRoomList(
            Long userId) { //<- token 생성 함수 만들기전 임시 방편 @RequestHeader("Authorization") String token
        var data = chatService.getChatRoomList(userId);
        return ResponseEntity.ok(new ResponseForm(ChatRoomType.CHAT_ROOM_LIST.getMessage(), data));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createChatRoomList(Long buyerId, Long sellerId,
            Long itemId) {
        var data = chatService.createChatRoom(buyerId, sellerId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomType.CREATE_CHAT_ROOM.getMessage(), data));
    }

}

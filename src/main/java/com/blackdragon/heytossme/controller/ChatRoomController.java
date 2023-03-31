package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import com.blackdragon.heytossme.type.resposne.ChatRoomResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("v1/chat/rooms")
@RequiredArgsConstructor
@Slf4j
public class ChatRoomController {

    private final ChatService chatService;
    private final String USER_ID = "userId";

    @GetMapping
    public ResponseEntity<ResponseForm> getChatRoomList(HttpServletRequest httpRequest) {
        log.info("getChatRoomList start");

        Long userId = (Long) httpRequest.getAttribute(USER_ID);
        var data = chatService.getChatRoomList(userId);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CHAT_ROOM_LIST.getMessage(), data)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createChatRoomList(HttpServletRequest httpRequest,
            @RequestParam("item-id") Long itemId) {
        log.info("createChatRoomList start");

        Long userId = (Long) httpRequest.getAttribute(USER_ID);
        var data = chatService.createChatRoom(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CREATE_CHAT_ROOM.getMessage(), data)
        );
    }

    @DeleteMapping("/{room-id}")
    public ResponseEntity<ResponseForm> deleteChatRoom(HttpServletRequest httpRequest,
            @PathVariable("room-id") Long roomId) {
        log.info("deleteChatRoom start");

        Long userId = (Long) httpRequest.getAttribute(USER_ID);
        chatService.deleteChatRoom(userId, roomId);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.DELETE_CHAT_ROOM.getMessage(), null)
        );
    }

    @PostMapping("/{room-id}/account-share")
    public ResponseEntity<ResponseForm> convertAccountTransferStatus(HttpServletRequest httpRequest,
            @PathVariable("room-id") Long roomId, @RequestParam("status") boolean status) {
        log.info("convertAccountTransferStatus start");

        Long userId = (Long) httpRequest.getAttribute(USER_ID);
        var data = chatService.convertAccountTransferStatus(userId, roomId, status);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CONVERT_ACCOUNT_TRANSFER_STATUS.getMessage(),
                        data)
        );
    }

}


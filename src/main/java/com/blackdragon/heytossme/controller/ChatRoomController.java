package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ChatRoomDto.ConvertAccountStatusRequest;
import com.blackdragon.heytossme.dto.ChatRoomDto.CreateRequest;
import com.blackdragon.heytossme.dto.ChatRoomDto.DeleteRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import com.blackdragon.heytossme.type.ChatRoomResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping
    public ResponseEntity<ResponseForm> getChatRoomList(
            @RequestParam("userId") Long userId) { //<- token 생성 함수 만들기전 임시 방편 @RequestHeader("Authorization") String token
        var data = chatService.getChatRoomList(userId);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CHAT_ROOM_LIST.getMessage(), data)
        );
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createChatRoomList(@RequestBody CreateRequest request) {
        var data = chatService.createChatRoom(request);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CREATE_CHAT_ROOM.getMessage(), data)
        );
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> deleteChatRoom(@RequestBody DeleteRequest request) {
        chatService.deleteChatRoom(request);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.LEAVE_CHAT_ROOM.getMessage(), null)
        );
    }

    @PostMapping("/account-share")
    public ResponseEntity<ResponseForm> convertAccountTransferStatus(@RequestBody
    ConvertAccountStatusRequest request) {
        var data = chatService.convertAccountTransferStatus(request);
        return ResponseEntity.ok(
                new ResponseForm(ChatRoomResponse.CONVERT_ACCOUNT_TRANSFER_STATUS.getMessage(),
                        data)
        );
    }

}


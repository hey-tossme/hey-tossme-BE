package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.request.ChatRoomRequest;
import com.blackdragon.heytossme.dto.response.ResponseForm;
import com.blackdragon.heytossme.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatMessages")
@RequiredArgsConstructor
public class ChatMessageController {

    @GetMapping
    public ResponseEntity<ResponseForm> chatRoomList(@RequestHeader("Authorization") String token) {

        return null;
    }

    @PostMapping
    public ResponseEntity<ResponseForm> createRoomByBuyer(@RequestHeader("Authorization") String token,
                                                          @RequestBody ChatRoomRequest.Create request) {

        return null;
    }

}

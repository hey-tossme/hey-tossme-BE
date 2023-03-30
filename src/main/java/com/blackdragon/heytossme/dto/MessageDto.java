package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.ChatMessage;
import lombok.Data;

public class MessageDto {

    @Data
    public static class SendMessage {

        private Long chatRoomId;
        private Long senderId;
        private String userName;
        private String profileUrl;
        private String message;
    }

    @Data
    public static class Response {

        private Long id;
        private MemberDto.Response sender;
        private String message;

        public Response(ChatMessage chatMessage) {
            this.id = chatMessage.getId();
            this.sender = new MemberDto.Response(chatMessage.getSender());
            this.message = chatMessage.getMessage();
        }
    }

}

package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.ChatRoom;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

public class ChatRoomDto {


    @Data
    @Slf4j
    public static class Response {

        private Long id;
        private MemberDto.Response buyer;
        private MemberDto.Response seller;
        private ItemDto.Response item;
        private String lastMessage;
        private boolean accountTransferStatus;

        public Response(ChatRoom chatRoom) {
            log.info("chatRoom = {}", chatRoom.getChatMessageList());
            int lastMessageCounter = chatRoom.getChatMessageList() == null ? 0
                    : chatRoom.getChatMessageList().size();
            this.id = chatRoom.getId();
            this.buyer = new MemberDto.Response(chatRoom.getBuyer());
            this.seller = new MemberDto.Response(chatRoom.getSeller());
            this.item = new ItemDto.Response(chatRoom.getItem());
            this.lastMessage = lastMessageCounter == 0 ? "내용이 없습니다"
                    : chatRoom.getChatMessageList().get(lastMessageCounter - 1).getMessage();
            this.accountTransferStatus = chatRoom.isAccountTransferStatus();
        }
    }

}

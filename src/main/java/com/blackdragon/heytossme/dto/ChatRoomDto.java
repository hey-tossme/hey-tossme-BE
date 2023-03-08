package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.ChatRoom;
import java.util.Objects;
import lombok.Data;

public class ChatRoomDto {

    @Data
    public static class Response {

        private Long id;
        //        private MemberDto.Response buyer;
//        private MemberDto.Response seller;
//        private ItemDto.Response item;
        private Long buyer;
        private Long seller;
        private Long item;
        private String lastMessage;
        private boolean accountTransferStatus;

        public Response(ChatRoom chatRoom) {
            this.id = chatRoom.getId();
//            this.buyer = new MemberDto.Response(chatRoom.getBuyer());
//            this.seller = new MemberDto.Response(chatRoom.getSeller());
//            this.item = new ItemDto.Response(chatRoom.getItem());
            this.buyer = chatRoom.getBuyer().getId();
            this.seller = chatRoom.getSeller().getId();
            this.item = chatRoom.getItem().getId();
            this.lastMessage = Objects.requireNonNull(chatRoom.getChatMessageQueue().peek())
                    .getMessage();
            this.accountTransferStatus = chatRoom.isAccountTransferStatus();
        }
    }

}

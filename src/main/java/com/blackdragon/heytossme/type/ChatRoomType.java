package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum ChatRoomType {


    CHAT_ROOM_LIST("successfully get chatroom list"),

    CREATE_CHAT_ROOM("successfully create chatroom"),

    LEAVE_CHAT_ROOM("Successfully leave chatroom");

    ChatRoomType(String message) {
        this.message = message;
    }

    final String message;
}

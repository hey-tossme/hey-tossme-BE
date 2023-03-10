package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum ChatRoomResponse {

    CHAT_ROOM_LIST("successfully get chatroom list"),
    CREATE_CHAT_ROOM("successfully create chatroom"),

    LEAVE_CHAT_ROOM("Successfully leave chatroom");

    ChatRoomResponse(String message) {
        this.message = message;
    }

    final String message;
}

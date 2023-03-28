package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum ChatRoomResponse {

    CHAT_ROOM_LIST("successfully get chatroom list"),
    CREATE_CHAT_ROOM("successfully create chatroom"),
    CONVERT_ACCOUNT_TRANSFER_STATUS("successfully convert account transfer status"),
    DELETE_CHAT_ROOM("Successfully delete chatroom"),
    GET_MESSAGE_LIST("Successfully get message list");

    ChatRoomResponse(String message) {
        this.message = message;
    }

    final String message;
}

package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatErrorCode implements BaseErrorCodeImpl {
    NOT_ACCEPTABLE_USER(HttpStatus.NOT_ACCEPTABLE, "User does not belong to the room"),
    USER_MISMATCH_TO_SELLER(HttpStatus.NOT_ACCEPTABLE, "User is not the seller"),
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Chatroom not found");

    private final HttpStatus httpStatus;
    private final String message;
}

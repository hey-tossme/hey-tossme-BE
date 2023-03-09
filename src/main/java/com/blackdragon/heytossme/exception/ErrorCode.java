package com.blackdragon.heytossme.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    //공통
    INSUFFICIENT_PARAMETER(HttpStatus.BAD_REQUEST, "Not sufficient parameter"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token supplied"),
    //회원 가입에러
    CONFLICT_EMAIL(HttpStatus.CONFLICT, "Already existed email"),
    INCORRECT_AUTH_CODE(HttpStatus.CONFLICT, "Incorrect authorize code"),
    //회원 정보
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, "Incorrect current password"),
    DIFFERENT_PASSWORD_REQUIRED(HttpStatus.CONFLICT, "Different password required"),
    //상품 정보
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Item not found"),
    //채팅방 정보
    CHAT_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, "Chatroom not found");


    ErrorCode(HttpStatus statusCode, String message) {
        this.httpStatus = statusCode;
        this.message = message;
    }

    final HttpStatus httpStatus;
    final String message;
}

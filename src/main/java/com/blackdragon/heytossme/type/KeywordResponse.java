package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum KeywordResponse {

    GET_KEYWORD_LIST("successfully find keyword"),
    REGISTER_KEYWORD("successfully registered keyword"),
    DELETE_KEYWORD("successfully deleted keyword");

    final String message;

    KeywordResponse(String message) {
        this.message = message;
    }
}

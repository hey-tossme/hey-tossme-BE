package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum MemberResponse {
    SIGN_UP("Successfully signed up"),
    FIND_INFO("successfully get user info"),
    CHANGE_INFO("successfully edit info"),

    DELETE_USER("successfully deleted"),

    SIGN_IN("successfully signed up");

    MemberResponse(String message) {
        this.message = message;
    }

    final String message;
}

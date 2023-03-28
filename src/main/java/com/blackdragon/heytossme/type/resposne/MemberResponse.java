package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum MemberResponse {
    SIGN_UP("Successfully signed up"),
    FIND_INFO("successfully get user info"),
    CHANGE_INFO("successfully edit info"),
    DELETE_USER("successfully deleted"),
    SIGN_IN("successfully signed up"),
    SEND_EMAIL("successfully email sent"),
    MATCH_CODE("successfully authorized"),
    RESET_PASSWORD("successfully reset password"),
    RE_CREATED_ACCESS_TOKEN("successfully re-created token"),
    SIGN_OUT("LOGOUT");


    MemberResponse(String message) {
        this.message = message;
    }

    final String message;
}

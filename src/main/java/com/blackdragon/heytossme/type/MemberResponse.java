package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum MemberResponse {
    SING_UP("Successfully signed up"),
    SIGN_OUT("logout");

    MemberResponse(String message) {
        this.message = message;
    }

    final String message;
}

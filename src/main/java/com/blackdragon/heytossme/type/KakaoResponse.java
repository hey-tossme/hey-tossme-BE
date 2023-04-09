package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum KakaoResponse {

    LOG_IN("Successfully Kakao Login In");

    final String message;

    KakaoResponse(String message) {
        this.message = message;
    }
}

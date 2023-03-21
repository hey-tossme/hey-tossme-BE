package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum MailResponse {

    SENT_MAIL("successfully email sent"),
    AUTH_MAIL("successfully authorized");

    private final String message;

    MailResponse(String message) {
        this.message = message;
    }
}

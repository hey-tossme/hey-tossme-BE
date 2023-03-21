package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum UploadResponse {
    SENT_URL("successfully url sent");

    private final String message;

    UploadResponse(String message) {
        this.message = message;
    }
}

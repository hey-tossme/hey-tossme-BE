package com.blackdragon.heytossme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseForm {
    private String message;
    private Object data;
    private String token;

    public ResponseForm(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}

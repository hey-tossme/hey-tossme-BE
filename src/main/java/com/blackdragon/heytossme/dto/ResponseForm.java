package com.blackdragon.heytossme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ResponseForm {

    private String message;
    private Object data;
    private String token;

    public ResponseForm(String message, Object data) {
        this.message = message;
        this.data = data;
    }
}

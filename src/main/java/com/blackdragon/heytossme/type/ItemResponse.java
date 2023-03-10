package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum ItemResponse {
    REGISTER_ITEM("Successfully item registered");

    ItemResponse(String message) {
        this.message = message;
    }

    final String message;
}

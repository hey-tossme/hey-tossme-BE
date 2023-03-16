package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum ItemResponse {
    GET_ITEM_LIST("Successfully get item list"),
    REGISTER_ITEM("Successfully item registered");

    ItemResponse(String message) {
        this.message = message;
    }

    final String message;
}

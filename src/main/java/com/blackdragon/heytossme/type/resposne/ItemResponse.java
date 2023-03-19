package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum ItemResponse {
    GET_ITEM_LIST("Successfully get item list"),
    GET_DETAIL("Successfully get item detail"),
    REGISTER_ITEM("Successfully item registered");


    ItemResponse(String message) {
        this.message = message;
    }

    final String message;
}

package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum ItemResponse {
    GET_ITEM_LIST("Successfully get item list"),
    GET_DETAIL("Successfully get item detail"),
    MODIFY_DETAIL("Successfully modify item detail"),
    DELETE_ITEM("Successfully delete item detail"),
    DEAL_CONFIRM("Successfully confirm deal"),
    GET_SELL_LIST("Successfully get sell list"),
    GET_BUY_LIST("Successfully get buy list"),
    GET_ADDRESS_LIST("Successfully get address list"),
    REGISTER_ITEM("Successfully item registered");


    ItemResponse(String message) {
        this.message = message;
    }

    final String message;
}

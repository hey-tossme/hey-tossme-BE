package com.blackdragon.heytossme.type.resposne;

import lombok.Getter;

@Getter
public enum BookmarkResponse {

    GET_BOOKMARK_LIST("successfully find bookmarks"),
    REGISTER_BOOKMARK("successfully registered bookmark"),
    DELETE_BOOKMARK("successfully deleted bookmark");

    final String message;

    BookmarkResponse(String message) {
        this.message = message;
    }

}

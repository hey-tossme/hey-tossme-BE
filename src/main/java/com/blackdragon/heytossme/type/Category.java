package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum Category {
    ACCOMMODATION("accommodation"),
    RESTAURANT("restaurant"),
    BEAUTY("beauty"),
    CONCERT("concert"),
    ACTIVITY("activity");

    Category(String toRealName) {
        this.toRealName = toRealName;
    }

    final String toRealName;

    public static Category findBy(String arg) {
        for (Category category : values()) {
            if (arg.equals(category.toRealName)) {
                return category;
            }
        }
        return null;
    }
}

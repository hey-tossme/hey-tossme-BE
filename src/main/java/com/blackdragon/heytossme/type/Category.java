package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum Category {
    ACCOMMODATION("숙박"),
    RESTAURANT("레스토랑"),
    BEAUTY("미용실"),
    CONCERT("전시공연"),
    ACTIVITY("액티비티");

    Category(String korean) {
        this.toKorean = korean;
    }

    final String toKorean;

    public static Category findBy(String arg) {
        for (Category category : values()) {
            if (arg.equals(category.toKorean)) {
                return category;
            }
        }
        return null;
    }
}

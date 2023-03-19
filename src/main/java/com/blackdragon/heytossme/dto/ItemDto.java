package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Item;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Data;

public class ItemDto {

    @Data
    public static class ItemRequest {

        @NotBlank
        private String category;
        @NotBlank
        private String title;
        private int price;
        @NotBlank
        private String dueDate;
        private String contents;
        private String address;
        private String addressDetail;
        private String imageUrl;
        private String status;
    }

    @Data
    public static class Response {

        private Long id;
        private MemberDto.Response seller;
        private String category;
        private String title;
        private String contents;
        private int price;
        private LocalDateTime createdAt;
        private LocalDateTime dueTime;
        private float latitude;
        private float longitude;
        private String imageUrl;
        private String status;

        public Response(Item item) {
            this.id = item.getId();
            this.seller = new MemberDto.Response(item.getMember());
            this.category = item.getCategory().getToKorean();
            this.title = item.getTitle();
            this.contents = item.getContents();
            this.price = item.getPrice();
            this.createdAt = item.getCreatedAt();
            this.dueTime = item.getDueDate();
            this.latitude = item.getLatitude();
            this.longitude = item.getLongitude();
            this.imageUrl = item.getImageUrl();
            this.status = item.getStatus().name();
        }
    }
}

package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Bookmark;
import com.blackdragon.heytossme.type.Category;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class BookmarkDto {

    @Data
    public static class CreateRequest {

        @NotBlank
        private Long userId;
        @NotBlank
        private Long itemId;
    }

    @Builder
    @Data
    public static class CreateResponse {

        private Long id;
        private Long itemId;
        private Long userId;
        private Category category;
        private String title;
        private String contents;
        private int price;
        private LocalDateTime createdAt;
        private LocalDateTime dueTime;
        private float latitude;
        private float longitude;
        private String imageUrl;
        private String status;

        public static CreateResponse from(Bookmark bookmark) {
            return CreateResponse.builder()
                    .id(bookmark.getId())
                    .itemId(bookmark.getItem().getId())
                    .userId(bookmark.getMember().getId())
                    .category(bookmark.getItem().getCategory())
                    .title(bookmark.getItem().getTitle())
                    .contents(bookmark.getItem().getContents())
                    .price(bookmark.getItem().getPrice())
                    .createdAt(bookmark.getCreatedAt())
                    .dueTime(bookmark.getItem().getDueDate())
                    .latitude(bookmark.getItem().getLatitude())
                    .longitude(bookmark.getItem().getLongitude())
                    .imageUrl(bookmark.getItem().getImageUrl())
                    .status(bookmark.getItem().getStatus().name())
                    .build();
        }
    }

    @Builder
    @Data
    public static class DeleteResponse {

        private Long id;
        private Long userId;
        private Category category;
        private String title;
        private String contents;
        private int price;
        private LocalDateTime createdAt;
        private LocalDateTime dueTime;
        private float latitude;
        private float longitude;
        private String imageUrl;
        private String status;

        public static DeleteResponse from(Bookmark bookmark) {
            return DeleteResponse.builder()
                    .id(bookmark.getId())
                    .userId(bookmark.getMember().getId())
                    .category(bookmark.getItem().getCategory())
                    .title(bookmark.getItem().getTitle())
                    .contents(bookmark.getItem().getContents())
                    .price(bookmark.getItem().getPrice())
                    .createdAt(bookmark.getCreatedAt())
                    .dueTime(bookmark.getItem().getDueDate())
                    .latitude(bookmark.getItem().getLatitude())
                    .longitude(bookmark.getItem().getLongitude())
                    .imageUrl(bookmark.getItem().getImageUrl())
                    .status(bookmark.getItem().getStatus().name())
                    .build();
        }
    }
}

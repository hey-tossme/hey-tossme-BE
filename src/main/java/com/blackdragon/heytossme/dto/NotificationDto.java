package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.persist.entity.Notification;
import com.blackdragon.heytossme.type.NotificationType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class NotificationDto {

    @Builder
    @Data
    public static class Response {

        @NotBlank
        private Long id;
        @NotBlank
        private Long itemId;
        private String message;
        private boolean readOrNot;
        private LocalDateTime createdAt;
		private NotificationType type;
		private String title;

        public static Response from(Notification notification) {
            return Response.builder()
                    .id(notification.getId())
                    .itemId(notification.getItem().getId())
                    .message(notification.getMessage())
                    .readOrNot(notification.isReadOrNot())
					.type(notification.getType())
					.title(notification.getItem().getTitle())
                    .createdAt(notification.getCreatedAt())
                    .build();
        }
    }

	@Data
	public static class StatusChangeResponse{
		private Long notificationId;
		private boolean readOrNot;
	}

	@Data
	@Builder
	public static class NotificationRequest {

		private String registrationToken;
		private String title;
		private String body;
		private NotificationType type;
		private Member member;
		private Item item;
		private String message;

	}
}

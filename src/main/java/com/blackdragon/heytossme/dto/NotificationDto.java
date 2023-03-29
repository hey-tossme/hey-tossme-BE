package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Notification;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class NotificationDto {

	@Builder
	@Data
	public static class Response{

		@NotBlank
		private Long id;
		@NotBlank
		private Long itemId;
		private String message;
		private boolean readOrNot;
		private LocalDateTime createdAt;

		public static Response from(Notification notification) {
			return Response.builder()
					.id(notification.getId())
					.itemId(notification.getItem().getId())
					.message(notification.getMessage())
					.readOrNot(notification.isReadOrNot())
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
	public static class NotificationRequest {

		private String token;
		private String title;
		private String message;
	}
}

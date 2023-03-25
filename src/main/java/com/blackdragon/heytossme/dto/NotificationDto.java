package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Notification1;
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

		public static Response from(Notification1 notification) {
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
	public static class statusChangeResponse{
		private Long notificationId;
		private boolean readOrNot;
	}
}

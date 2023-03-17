package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Bookmark;
import jakarta.validation.constraints.NotBlank;
import java.awt.print.Book;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

public class BookmarkDto {

	@Data
	public static class CreateRequest{

		@NotBlank
		private Long userId;
		@NotBlank
		private Long itemId;
	}

	@Builder
	@Data
	public static class CreateResponse{

		private Long id;
		private Long itemId;
		private Long userId;
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

//		public CreateResponse(Bookmark bookmark) {
//			this.id = bookmark.getId();
//			this.itemId = bookmark.getItem().getId();
//			this.userId = bookmark.getMember().getId();
//			this.category = bookmark.getItem().getCategory();
//			this.title = bookmark.getItem().getTitle();
//			this.contents = bookmark.getItem().getContents();
//			this.price = bookmark.getItem().getPrice();
//			this.createdAt = bookmark.getCreatedAt();
//			this.dueTime = bookmark.getItem().getDueDate();
//			this.latitude = bookmark.getItem().getLatitude();
//			this.longitude = bookmark.getItem().getLongitude();
//			this.imageUrl = bookmark.getItem().getImageUrl();
//			this.status = bookmark.getItem().getStatus();
//		}

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
					.status(bookmark.getItem().getStatus())
					.build();
		}
	}

	@Builder
	@Data
	public static class DeleteResponse{

		private Long id;
		private Long userId;
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
					.status(bookmark.getItem().getStatus())
					.build();
		}
	}
}

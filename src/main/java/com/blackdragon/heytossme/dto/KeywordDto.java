package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Keyword;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

public class KeywordDto {

	@Builder
	@Data
	public static class Response{

		@NotBlank
		private Long id;
		@NotBlank
		private String keyword;

		public static Response from(Keyword keyword) {
			return Response.builder()
					.id(keyword.getId())
					.keyword(keyword.getKeyword())
					.build();
		}
	}

}

package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Member;
import jakarta.servlet.http.Cookie;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

public class MemberDto {

    @Data
    public static class SignUpRequest {

        @Email
        private String email;
        @NotBlank
        private String name;
        @NotBlank
        private String password;
        private String imageUrl;
        private String socialType;
        private String account;
        private String bankName;
    }

    @Data
    public static class Response {

        private Long id;
        private String email;
        private String name;
        private String imageURL;
        private String socialType;
        private String status;
        private String account;
        private String bankName;

        public Response(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.imageURL = member.getImageUrl();
            this.socialType = member.getSocialLoginType();
            this.status = member.getStatus();
            this.account = member.getAccount();
            this.bankName = member.getBankName();
        }
    }

    @Data
    public static class SignInRequest {

        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class SignInResponse {

        private Long id;
        private String account;

    }

    @Data
    @Builder
    public static class ResponseToken {

        private String refreshToken;
        private String accessToken;
    }

    @Data
    @Builder
    public static class SignOutResponse {

        private Long id;
        private String email;
        private String name;

        public static SignOutResponse from(Member member) {
            return SignOutResponse.builder()
                    .id(member.getId())
                    .email(member.getEmail())
                    .name(member.getName())
                    .build();
        }
    }

    @Data
    @AllArgsConstructor
    public static class AuthResponse{
        private String refreshToken;
        private Cookie cookie;

    }
}

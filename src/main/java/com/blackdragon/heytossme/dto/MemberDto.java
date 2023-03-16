package com.blackdragon.heytossme.dto;

import com.blackdragon.heytossme.persist.entity.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseCookie;
import lombok.NoArgsConstructor;

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
    public static class ModifyRequest {
        private String email;
        private String name;
        private String curPassword;
        private String password;
        private String imageUrl;
        private String socialType;
        private String account;
        private String bankName;
    }

    @Data
    @NoArgsConstructor
    public static class Response {

        private Long id;
        private String email;
        private String name;
        private String imageURL;
        private String socialType;
        private LocalDateTime createdAt;
        private String status;
        private String account;
        private String bankName;

        private String token;

        public Response(Member member) {
            this.id = member.getId();
            this.email = member.getEmail();
            this.name = member.getName();
            this.imageURL = member.getImageUrl();
            this.socialType = member.getSocialLoginType();
            this.createdAt = member.getCreatedAt();
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
    public static class ResponseToken{

        private ResponseCookie responseCookie;
        private String accessToken;
    }
}

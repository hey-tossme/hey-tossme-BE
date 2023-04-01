package com.blackdragon.heytossme.persist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Builder
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String email;

    @NotNull
    private String name;

    @NotNull
    private String password;

    private String pwAuthKey;

    private String imageUrl;

    private String socialLoginType;

    @NotNull
    private String status;

    private String account;

    private String bankName;
    private String registrationToken;    //fcm token

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setPwAuthKey(String pwAuthKey) {
        this.pwAuthKey = pwAuthKey;
    }
}

package com.blackdragon.heytossme.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
public class MailDto {

    @Email
    private String email;
    private String code;
}

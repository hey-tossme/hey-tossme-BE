package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MailDto;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.MailService;
import com.blackdragon.heytossme.type.MailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<ResponseForm> send(@RequestBody MailDto mailDto) {

        mailService.send(mailDto);

        return ResponseEntity.ok(new ResponseForm(MailResponse.SENT_MAIL.getMessage(), mailDto));
    }

    @PostMapping("/validate")
    public ResponseEntity<ResponseForm> validate(@RequestBody MailDto mailDto) {

        mailService.validate(mailDto);

        return ResponseEntity.ok(new ResponseForm(MailResponse.AUTH_MAIL.getMessage(), mailDto));
    }
}

package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.MailDto;
import com.blackdragon.heytossme.persist.MailRepository;
import com.blackdragon.heytossme.persist.entity.EmailCertification;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final MailRepository mailRepository;
    private final String emailAuthKey = createKey();

    public void send(MailDto mailDto) {
        log.info(mailDto.getEmail());
        validate(mailDto.getEmail());
        checkDuplicate(mailDto.getEmail());
        MimeMessage message = createMessage(mailDto.getEmail());
        mailSender.send(message);
    }
    public void validate(MailDto mailDto) {
        EmailCertification emailCertification = mailRepository.findById(mailDto.getEmail())
                .orElseThrow(() -> new RuntimeException("이메일이 없습니다"));
        if (!emailCertification.getEmailAuthKey().equals(mailDto.getCode())) {
            throw new RuntimeException("인증번호 불일치");
        }
    }
}
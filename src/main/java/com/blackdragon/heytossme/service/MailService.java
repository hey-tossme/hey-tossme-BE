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
    private MimeMessage createMessage(String email) {
        System.out.println("보내는 대상 : " + email);
        System.out.println("인증 번호 : " + email);
        MimeMessage message = mailSender.createMimeMessage();
        try {
            message.addRecipients(RecipientType.TO, email);//보내는 대상
            message.setSubject("이메일 인증 테스트");//제목
            String msg = "";
            msg += "<div style='margin:20px;'>";
            msg += "<h1> 안녕하세요 hey-toss-me 입니다. </h1>";
            msg += "<br>";
            msg += "<p>아래 인증코드를 복사해 입력해주세요<p>";
            msg += "<br>";
            msg += "<p>감사합니다.<p>";
            msg += "<br>";
            msg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
            msg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
            msg += "<div style='font-size:130%'>";
            msg += "CODE : <strong>";
            msg += emailAuthKey + "</strong><div><br/> ";
            msg += "</div>";
            message.setText(msg, "utf-8", "html");//내용
            message.setFrom(new InternetAddress("heytossme@gmail.com", "heytossme"));//보내는 사람

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
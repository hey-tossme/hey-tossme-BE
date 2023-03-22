package com.blackdragon.heytossme.component;

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
import org.springframework.stereotype.Component;

@Component
@Log4j2
@RequiredArgsConstructor
public class MailComponent {

    private final JavaMailSender mailSender;

    public void send(String email, String authKey) {
        validate(email);
        MimeMessage message = createMessage(email, authKey);
        mailSender.send(message);
    }

    private MimeMessage createMessage(String email, String authKey) {
        log.info("보내는 대상 : " + email);
        log.info("인증 번호 : " + authKey);
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
            msg += authKey + "</strong><div><br/> ";
            msg += "</div>";
            message.setText(msg, "utf-8", "html");//내용
            message.setFrom(new InternetAddress("heytossme@gmail.com", "heytossme"));//보내는 사람

        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }

    public String createKey() {
        StringBuilder key = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) {
            int index = rnd.nextInt(3);

            switch (index) {
                case 0 -> key.append((char) (rnd.nextInt(26) + 97));
                case 1 -> key.append((char) (rnd.nextInt(26) + 65));
                case 2 -> key.append((rnd.nextInt(10)));
            }
        }
        return key.toString();
    }

    private void validate(String email) {
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException e) {
            throw new IllegalArgumentException("유효하지 않은 이메일 주소입니다: " + email, e);
        }
    }
}

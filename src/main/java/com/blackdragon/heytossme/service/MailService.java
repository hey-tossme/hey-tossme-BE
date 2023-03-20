package com.blackdragon.heytossme.service;

import static com.blackdragon.heytossme.exception.errorcode.MailErrorCode.ALREADY_EXIST_EMAIL;
import static com.blackdragon.heytossme.exception.errorcode.MailErrorCode.INCORRECT_CODE;

import com.blackdragon.heytossme.component.MailComponent;
import com.blackdragon.heytossme.dto.MailDto;
import com.blackdragon.heytossme.exception.MailException;
import com.blackdragon.heytossme.persist.MailRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class MailService {

    private final MailComponent mailComponent;
    private final MailRepository mailRepository;
    private final MemberRepository memberRepository;

    public void send(MailDto mailDto) {
        String email = mailDto.getEmail();

        if (memberRepository.findByEmail(email).isPresent()) {
            throw new MailException(ALREADY_EXIST_EMAIL);
        }

        String authKey = mailComponent.createKey();

        mailRepository.save(Mail.builder()
                .email(email)
                .emailAuthKey(authKey)
                .build());

        mailComponent.send(email, authKey);
    }

    public void validate(MailDto mailDto) {
        // 추후 수정 요망
        Mail mail = mailRepository.findById(mailDto.getEmail())
                .orElseThrow(RuntimeException::new);

        if (!mail.getEmailAuthKey().equals(mailDto.getCode())) {
            throw new MailException(INCORRECT_CODE);
        }
    }
}

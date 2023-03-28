package com.blackdragon.heytossme.service;


import static com.blackdragon.heytossme.exception.errorcode.MemberErrorCode.INCORRECT_CODE;
import static com.blackdragon.heytossme.exception.errorcode.MemberErrorCode.INCORRECT_PASSWORD;
import static com.blackdragon.heytossme.exception.errorcode.MemberErrorCode.MEMBER_NOT_FOUND;

import com.blackdragon.heytossme.component.AuthExtractor;
import com.blackdragon.heytossme.component.MailComponent;
import com.blackdragon.heytossme.component.TokenProvider;
import com.blackdragon.heytossme.dto.MemberDto;
import com.blackdragon.heytossme.dto.MemberDto.AuthResponse;
import com.blackdragon.heytossme.dto.MemberDto.DeleteRequest;
import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.PasswordRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignOutResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.MemberSocialType;
import com.blackdragon.heytossme.type.MemberStatus;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;
    private final AuthExtractor authExtractor;
    private final MailComponent mailComponent;


    public MemberDto.Response signUp(SignUpRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new MemberException(MemberErrorCode.MEMBER_NOT_FOUND);
        }
        Member member = memberRepository.save(
                Member.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .imageUrl(request.getImageUrl())
                        .socialLoginType(MemberSocialType.EMAIL.name())
                        .status(MemberStatus.NORMAL.name())
                        .account(request.getAccount())
                        .bankName(request.getBankName())
                        .build()
        );
        return new Response(member);
    }

    public Member signIn(SignInRequest request) {

        Optional<Member> byEmail = memberRepository.findByEmail(request.getEmail());

        Member member = byEmail.orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(MemberErrorCode.INCORRECT_PASSWORD);
        }

//        member.setMessageToken(messageToken);   //fcm토큰을 db에저장하고 서버측에서 관리

        return member;
    }

    public Response getInfo(Long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return modelMapper.map(member, Response.class);
    }

    public Response modifyInfo(Long userId, ModifyRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        if (request.getImageUrl() != null) {
            member.setImageUrl(request.getImageUrl());
        }
        if (request.getAccount() != null) {
            member.setAccount(request.getAccount());
        }
        if (request.getBankName() != null) {
            member.setBankName(request.getBankName());
        }

        return modelMapper.map(member, Response.class);
    }

    public void deleteUser(Long userId, DeleteRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));
        // 사용자 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurPassword(), member.getPassword())) {
            throw new MemberException(INCORRECT_PASSWORD);
        }

        /*
          회원탈퇴 시 실제 DB 에서 Delete 를 하지 않고 유저 status 값을 탈퇴로 바꾼다
          탈퇴 처리한 유저가 다시 가입할 경우를 대비하여 이메일을 공백 값으로 둔다.
         */

        Member updateStatus = Member.builder()
                .id(member.getId())
                .email(" ")
                .name(member.getName())
                .password(" ")
                .imageUrl(member.getImageUrl())
                .socialLoginType(member.getSocialLoginType())
                .status(MemberStatus.QUIT.name())
                .build();

        memberRepository.save(updateStatus);
    }


    public ResponseToken generateToken(Long id, String email) {

        String accessToken = tokenProvider.generateToken(id, email, true);
        String refreshToken = tokenProvider.generateToken(id, email, false);

        return ResponseToken.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    public ResponseCookie generateCookie(String refreshToken) {

        return ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .sameSite("Lax")
                .httpOnly(true)
                .secure(false)
                .maxAge(1)
                .build();
    }

    public Cookie deleteCookie() {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");

        return cookie;
    }

    public SignOutResponse signOut(Long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setMessageToken(""); //fcm 토큰을 빈값으로 변경

        return MemberDto.SignOutResponse.from(member);
    }

    public String reCreateAccessToken(HttpServletRequest request
            , HttpServletResponse response, Long userId) {

        AuthResponse auth = authExtractor.extractRefreshToken(request);
        String refreshToken = auth.getRefreshToken();

        if (!tokenProvider.isExpiredRefreshToken(refreshToken)) {    //refresh 만료여부
            try {
                response.sendRedirect(request.getContextPath()
                        + "/v2/members/logout/" + refreshToken + "/" + userId);
            } catch (IOException e) {
                throw new MemberException(MemberErrorCode.INCORRECT_PATH);
            }
        }
        return tokenProvider.updateAccessToken(refreshToken);
    }

    //FCM 토큰 가져오기
    public String getFcmToken(Long userId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MEMBER_NOT_FOUND));

        return member.getMessageToken();
    }

    public void sendEmail(PasswordRequest request) {

        String email = request.getEmail();
        String authKey = mailComponent.createKey();
        mailComponent.send(email, authKey);
        memberRepository.findByEmail(email).ifPresent(
                member -> {
                    member.setPwAuthKey(authKey);
                    memberRepository.save(member);
                });
    }

    public void checkAuthCode(PasswordRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new MemberException(MEMBER_NOT_FOUND));

        if (!member.getPwAuthKey().equals(request.getCode())) {
            throw new MemberException(INCORRECT_CODE);
        }
    }

    public Response resetNewPassword(PasswordRequest request) {
        Member updatedMember = memberRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new MemberException(MEMBER_NOT_FOUND));

        updatedMember.setPassword(passwordEncoder.encode(request.getPassword()));
        memberRepository.save(updatedMember);

        return modelMapper.map(updatedMember, Response.class);

    }
}

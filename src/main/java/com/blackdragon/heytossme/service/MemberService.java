package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.component.TokenProvider;
import com.blackdragon.heytossme.dto.MemberDto;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.MemberStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final TokenProvider tokenProvider;

    public MemberDto.Response signUp(SignUpRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.CONFLICT_EMAIL);
        }
        Member member = memberRepository.save(
                Member.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .imageUrl(request.getImageUrl())
                        .socialLoginType(request.getSocialType())
                        .status(MemberStatus.NORMAL.name())
                        .build()
        );
        return new Response(member);
    }

    public Member signIn(SignInRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            //throw new MemberException(INCORRECT_PASSWORD);
            throw new CustomException(ErrorCode.INCORRECT_PASSWORD);
        }

        return member;
    }

    public ResponseToken generateToken(Long id, String email) {

        String accessToken = tokenProvider.generateToken(id, email);
        String refreshToken = tokenProvider.generateToken(id, email);

        //쿠키객체에 refresh token추가(쿠키 + 쿠키관련설정을 포함한 객체)
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .maxAge(86400000)   //쿠키 만료시간 = 1일
                .secure(true)
                .httpOnly(true)
                .sameSite("Strict")
                .build();

        // access token리턴, refresh
        return ResponseToken.builder()
                .responseCookie(cookie)
                .accessToken(accessToken)
                .build();
    }


}

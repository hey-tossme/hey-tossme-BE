package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignInResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignOutResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.MemberResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
//@CookieValue
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ResponseForm> signUp(@RequestBody SignUpRequest request) {
        var data = memberService.signUp(request);
        return ResponseEntity.ok(new ResponseForm(MemberResponse.SING_UP.getMessage(), data));
    }

    @PostMapping("/signIn")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request,
                                                            HttpServletResponse response) {

        Member member = memberService.signIn(request);
        ResponseToken tokens = memberService.generateToken(member.getId(), member.getEmail());
        Cookie cookie = memberService.generateCookie(tokens.getRefreshToken());

        SignInResponse data = SignInResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .build();

        response.addCookie(cookie);

        return ResponseEntity.ok()
                .body(new ResponseForm(
                        MemberResponse.SING_UP.getMessage(), data, tokens.getAccessToken()));
    }

    @PostMapping("/logout/auth")
    public ResponseEntity logout(HttpServletRequest request, HttpServletResponse response) {

        Long userId = (Long) request.getAttribute("userId");
        String token = (String) request.getAttribute("accessToken");

        SignOutResponse data = memberService.signOut(userId);

        Cookie cookie = memberService.deleteCookie();
        response.addCookie(cookie);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.SIGN_OUT.getMessage(), data, token)
        );
    }
}

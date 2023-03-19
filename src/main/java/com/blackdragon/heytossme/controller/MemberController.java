package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignInResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.resposne.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
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

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {

        Member member = memberService.signIn(request);
        ResponseToken tokens = memberService.generateToken(member.getId(), member.getEmail());

        SignInResponse data = SignInResponse.builder()
                .id(member.getId())
                .accessToken(tokens.getAccessToken())
                .build();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(HttpHeaders.SET_COOKIE, tokens.getResponseCookie().toString());

        return ResponseEntity.ok()
                .headers(responseHeaders)
                .body(new ResponseForm(MemberResponse.SING_UP.getMessage(), data));
    }
}

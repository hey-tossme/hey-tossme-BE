package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ResponseForm> signUp(@RequestBody SignUpRequest request) {
        var data = memberService.signUp(request);
        return ResponseEntity.ok(new ResponseForm(MemberResponse.SIGN_UP.getMessage(), data));
    }

    @GetMapping
    public ResponseEntity<ResponseForm> getInfo() {
        //Object userId = request.getAttribute("id");

        Response response = memberService.getInfo(3L);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.FIND_INFO.getMessage(), response));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request) {

        //로그인 하는 로직(웅준님 코드 삽입해야함)
        //Member member = memberService.signin()~~~~

        ResponseToken tokens =
                memberService.generateToken(1L, "bomilee.dev@gmail.com");//member.getId, member.getEmail로 수정요망

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokens.getResponseCookie().toString()).build();
    }

    @PatchMapping
    public ResponseEntity<ResponseForm> modifyInfo(@RequestBody ModifyRequest request) {

        Response response = memberService.modifyInfo(4L, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.CHANGE_INFO.getMessage(), response));
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> delete(@RequestBody ModifyRequest request) {

        memberService.deleteUser(6L, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.DELETE_USER.getMessage(), null));
    }
}

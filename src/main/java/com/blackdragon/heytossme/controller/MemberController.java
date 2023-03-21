package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto.DeleteRequest;
import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.resposne.MemberResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseForm> signUp(@Valid @RequestBody SignUpRequest request) {
        var data = memberService.signUp(request);
        return ResponseEntity.ok(new ResponseForm(MemberResponse.SIGN_UP.getMessage(), data));
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequest request) {
        ResponseToken tokens = memberService.signIn(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, tokens.getResponseCookie().toString()).build();
    }

    @GetMapping
    public ResponseEntity<ResponseForm> getInfo(HttpServletRequest httpServletRequest) {
        long id = (long) httpServletRequest.getAttribute("id");
        Response response = memberService.getInfo(id);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.FIND_INFO.getMessage(), response));
    }

    @PatchMapping
    public ResponseEntity<ResponseForm> modifyInfo(HttpServletRequest httpServletRequest,
            @Valid @RequestBody ModifyRequest request) {

        long id = (long) httpServletRequest.getAttribute("id");
        Response response = memberService.modifyInfo(id, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.CHANGE_INFO.getMessage(), response));
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> delete(HttpServletRequest httpServletRequest,
            @Valid @RequestBody DeleteRequest request) {

        long id = (long) httpServletRequest.getAttribute("id");
        memberService.deleteUser(id, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.DELETE_USER.getMessage(), null));
    }
}

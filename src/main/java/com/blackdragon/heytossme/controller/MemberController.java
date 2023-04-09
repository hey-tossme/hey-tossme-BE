package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto;
import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.ResponseToken;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignInResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignOutResponse;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.resposne.MemberResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";

    @PostMapping("/v2/members")
    public ResponseEntity<ResponseForm> signUp(@Valid @RequestBody SignUpRequest request) {
        var data = memberService.signUp(request);
        return ResponseEntity.ok(new ResponseForm(MemberResponse.SIGN_UP.getMessage(), data));
    }

    @PostMapping("/v2/members/signin")
    public ResponseEntity<?> signIn(@RequestBody SignInRequest request,
            HttpServletResponse response) {
        log.warn(">>>>>>>>> 프론트에서받은 fcm토큰 " + request.getFcmToken());
        Member member = memberService.signIn(request, request.getFcmToken());
        log.info(">>>>>>>>> 저장된 fcmtoken : " + member.getRegistrationToken());
        ResponseToken tokens = memberService.generateToken(member.getId(), member.getEmail());
        var cookie = memberService.generateCookie(tokens.getRefreshToken());

        SignInResponse data = SignInResponse.builder()
                .id(member.getId())
                .account(member.getAccount())
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok()
                .body(new ResponseForm(
                        MemberResponse.SIGN_UP.getMessage(), data, tokens.getAccessToken()));
    }

    /**
     * 로그아웃 API
     */
    @PostMapping("/v2/members/logout/{userId}")
    public ResponseEntity<ResponseForm> logout(@PathVariable("userId") Long _userId
            , HttpServletResponse response) {
        log.info("member logout start");
        Long userId = Long.valueOf(String.valueOf(_userId));

        Cookie cookie = memberService.deleteCookie();
        response.addCookie(cookie);

        SignOutResponse data = memberService.removeToken(userId);   //fcm토큰 초기화

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(
                new ResponseForm(MemberResponse.SIGN_OUT.getMessage(), data)
        );
    }

    /**
     * @param httpServletRequest
     * @return
     */

    @GetMapping("/v1/members")
    public ResponseEntity<ResponseForm> getInfo(HttpServletRequest httpServletRequest) {
        log.info("member getInfo start");

        Long id = (Long) httpServletRequest.getAttribute(USER_ID);
        Response response = memberService.getInfo(id);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.FIND_INFO.getMessage(), response));
    }

    @PatchMapping("/v1/members")
    public ResponseEntity<ResponseForm> modifyInfo(HttpServletRequest httpServletRequest,
            @Valid @RequestBody ModifyRequest request) {
        log.info("member modifyInfo start");

        Long id = (Long) httpServletRequest.getAttribute(USER_ID);
        log.info(String.valueOf(id));
        Response response = memberService.modifyInfo(id, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.CHANGE_INFO.getMessage(), response));
    }

    @DeleteMapping("/v1/members")
    public ResponseEntity<ResponseForm> delete(HttpServletRequest httpServletRequest) {
        log.info("member delete start");

        Long id = (Long) httpServletRequest.getAttribute(USER_ID);
        memberService.deleteUser(id);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.DELETE_USER.getMessage(), null));
    }

    //Access토큰 재발급
    @GetMapping("/v2/members/token/re-create/{userId}")
    public ResponseEntity<ResponseForm> recreateToken(HttpServletRequest request,
            HttpServletResponse response, @PathVariable Long userId) {
        log.info("member recreateToken start");

        //refresh만료시 405에러, refresh만료 안되면 200
        String generatedToken = memberService.reCreateAccessToken(request, response, userId);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.RE_CREATED_ACCESS_TOKEN.getMessage(),
                        null, generatedToken));
    }

    @PostMapping("/v2/members/reset-password")
    public ResponseEntity<ResponseForm> sendResetMail(
            @Valid @RequestBody MemberDto.PasswordRequest request) {
        log.info("member sendResetMail start");

        memberService.sendEmail(request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.SEND_EMAIL.getMessage(), request));
    }

    @PostMapping("/v2/members/reset-password/check")
    public ResponseEntity<ResponseForm> checkAuthCode(
            @Valid @RequestBody MemberDto.PasswordRequest request) {
        log.info("member checkAuthCode start");

        memberService.checkAuthCode(request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.MATCH_CODE.getMessage(), null));
    }

    @PatchMapping("/v2/members/reset-password")
    public ResponseEntity<ResponseForm> resetNewPassword(
            @Valid @RequestBody MemberDto.PasswordRequest request) {
        log.info("member resetNewPassword start");

        Response response = memberService.resetNewPassword(request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.RESET_PASSWORD.getMessage(), response));
    }
}

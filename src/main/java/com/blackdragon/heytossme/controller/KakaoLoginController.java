package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.KakaoLoginService;
import com.blackdragon.heytossme.type.KakaoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kakao")
@RequiredArgsConstructor
@Log4j2
public class KakaoLoginController {

    private final KakaoLoginService kakaoLoginService;

//    추후 삭제 예정
//    @GetMapping()
//    public String kakaoLogin() {
//
//        String url = "https://kauth.kakao.com/oauth/authorize?client_id=" + clientId
//                + "&redirect_uri=" + redirectUrl +
//                "&response_type=code";
//
//        return url;
//    }

    @GetMapping("/login")
    public ResponseEntity<?> oauthKakaoLogin(
            @RequestParam(value = "code", required = false) String code) {

        String accessToken = kakaoLoginService.getAccessToken(code);

        return ResponseEntity.ok(
                new ResponseForm(KakaoResponse.LOG_IN.getMessage(), accessToken));
    }
}
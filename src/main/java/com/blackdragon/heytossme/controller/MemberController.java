package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.MemberService;
import com.blackdragon.heytossme.type.MemberResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @PatchMapping
    public ResponseEntity<ResponseForm> modifyInfo(@RequestBody ModifyRequest request) {

        Response response = memberService.modifyInfo(4L, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.CHANGE_INFO.getMessage(), response));
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> delete(@RequestBody ModifyRequest request) {

        Response response = memberService.deleteUser(2L, request);

        return ResponseEntity.ok(
                new ResponseForm(MemberResponse.DELETE_USER.getMessage(), response));
    }
}

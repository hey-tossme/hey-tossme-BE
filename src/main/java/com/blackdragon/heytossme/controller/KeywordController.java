package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.KeywordDto.Response;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Keyword;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.service.KeywordService;
import com.blackdragon.heytossme.type.KeywordResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RequestMapping("/v1/keywords")
@RequiredArgsConstructor
@Controller
@Slf4j
public class KeywordController {

    private final KeywordService keywordService;
    private final MemberRepository memberRepository;

    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";

    @GetMapping
    public ResponseEntity<ResponseForm> getKeywords(HttpServletRequest request) {
        log.info("getKeywords start");

        Long userId = (Long) request.getAttribute(USER_ID);
        String token = (String) request.getAttribute(ACCESS_TOKEN);
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        List<Response> data = keywordService.getKeywordList(member.getId());

        return ResponseEntity.ok(
                new ResponseForm(KeywordResponse.GET_KEYWORD_LIST.getMessage(), data, token));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> registerKeyword(
            HttpServletRequest request, @RequestParam("keyword") String keyword) {
        log.info("registerKeyword start");

        Long userId = (Long) request.getAttribute(USER_ID);
        String token = (String) request.getAttribute(ACCESS_TOKEN);
        Response data = keywordService.registerKeyword(userId, keyword);

        return ResponseEntity.ok(
                new ResponseForm(KeywordResponse.REGISTER_KEYWORD.getMessage(), data, token));
    }

    @DeleteMapping("/{keyword}")
    public ResponseEntity<ResponseForm> deleteKeyword(HttpServletRequest request
            , @PathVariable("keyword") String keyword) {
        log.info("deleteKeyword start");

        Long userId = (Long) request.getAttribute(USER_ID);
        String token = (String) request.getAttribute(ACCESS_TOKEN);
        Keyword savedKeyword = keywordService.getKeyword(keyword);

        Response data = keywordService.deleteKeyword(savedKeyword.getKeyword());

        return ResponseEntity.ok(
                new ResponseForm(KeywordResponse.DELETE_KEYWORD.getMessage(), data, token));
    }
}

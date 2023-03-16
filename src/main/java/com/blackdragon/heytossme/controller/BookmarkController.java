package com.blackdragon.heytossme.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class BookmarkController {


    @GetMapping("/bookmarks")
    public ResponseEntity<?> getBookmarks(HttpServletRequest request) {
        Long userId = (Long)request.getAttribute("userId");
        String accessToken = (String) request.getAttribute("accessToken");
        log.info("controller 진입!" + userId + ", token: " + accessToken);

        return ResponseEntity.ok().build();
    }
}

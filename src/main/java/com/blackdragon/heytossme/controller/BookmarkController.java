package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.BookmarkService;
import com.blackdragon.heytossme.type.resposne.BookmarkResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/v1/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";

    @GetMapping
    public ResponseEntity<ResponseForm> getBookmarks(
            HttpServletRequest request,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "size", required = false) Integer size) {
        log.info("getBookmarks start");
        Long userId = (Long) request.getAttribute(USER_ID);
        String accessToken = (String) request.getAttribute(ACCESS_TOKEN);
        Page<CreateResponse> data = bookmarkService.getBookmarkList(userId, pageNum, size);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.GET_BOOKMARK_LIST.getMessage(), data,
                        accessToken));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> registerBookmarks(
            HttpServletRequest request, @RequestParam("itemId") Long itemId) {
        log.info("registerBookmarks start");

        Long userId = (Long) request.getAttribute(USER_ID);
        String accessToken = (String) request.getAttribute(ACCESS_TOKEN);
        CreateResponse data = bookmarkService.registerBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.REGISTER_BOOKMARK.getMessage(), data,
                        accessToken));
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> deleteBookmarks(
            HttpServletRequest request, @RequestParam("itemId") Long itemId) {
        log.info("deleteBookmarks start");

        Long userId = (Long) request.getAttribute(USER_ID);
        String accessToken = (String) request.getAttribute(ACCESS_TOKEN);
        DeleteResponse data = bookmarkService.deleteBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.DELETE_BOOKMARK.getMessage(), data, accessToken));
    }
}

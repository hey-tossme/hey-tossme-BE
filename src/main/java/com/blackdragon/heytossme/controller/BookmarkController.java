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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@RestController
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping("/auth")
    public ResponseEntity<ResponseForm> getBookmarks(
            HttpServletRequest request,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "size", required = false) Integer size) {
        Long userId = (Long) request.getAttribute("userId");
        String accessToken = (String) request.getAttribute("accessToken");
        Page<CreateResponse> data = bookmarkService.getBookmarkList(userId, pageNum, size);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.GET_BOOKMARK_LIST.getMessage(), data, accessToken));
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseForm> registerBookmarks(
            HttpServletRequest request, @RequestParam("item-id") Long itemId) {

        Long userId = (Long) request.getAttribute("userId");
        String accessToken = (String) request.getAttribute("accessToken");
        CreateResponse data = bookmarkService.registerBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.REGISTER_BOOKMARK.getMessage(), data, accessToken));
    }

    @DeleteMapping("/{item-id}/auth")
    public ResponseEntity<ResponseForm> deleteBookmarks(
            HttpServletRequest request, @PathVariable("item-id") Long itemId) {

        Long userId = (Long) request.getAttribute("userId");
        String accessToken = (String) request.getAttribute("accessToken");
        DeleteResponse data = bookmarkService.deleteBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.DELETE_BOOKMARK.getMessage(), data, accessToken));
    }
}

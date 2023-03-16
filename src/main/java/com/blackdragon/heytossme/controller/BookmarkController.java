package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.BookmarkDto.CreateRequest;
import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.BookmarkService;
import com.blackdragon.heytossme.type.BookmarkResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.print.Pageable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Controller
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<ResponseForm> getBookmarks(HttpServletRequest request) {
        Long userId = (Long)request.getAttribute("userId");
        List<CreateResponse> data = bookmarkService.getBookmarkList(userId);

        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.GET_BOOKMARK_LIST.getMessage(), data));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> registerBookmarks(HttpServletRequest request, Long itemId) {
        Long userId = (Long)request.getAttribute("userId");
        CreateResponse data = bookmarkService.registerBookmark(userId, itemId);

        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.REGISTER_BOOKMARK.getMessage(), data));
    }

    @DeleteMapping
    public ResponseEntity<ResponseForm> deleteBookmarks(HttpServletRequest request, Long itemId) {
        Long userId = (Long)request.getAttribute("userId");
        DeleteResponse data = bookmarkService.deleteBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.DELETE_BOOKMARK.getMessage(), data));
    }
}

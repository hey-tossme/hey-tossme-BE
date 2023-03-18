package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.BookmarkService;
import com.blackdragon.heytossme.type.BookmarkResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/bookmarks")
@Controller
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @GetMapping
    public ResponseEntity<ResponseForm> getBookmarks(HttpServletRequest request,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "size", required = false) Integer size) {

        Long userId = (Long)request.getAttribute("userId");
        List<CreateResponse> data = bookmarkService.getBookmarkList(userId, pageNum, size);

        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.GET_BOOKMARK_LIST.getMessage(), data));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> registerBookmarks(HttpServletRequest request,@RequestParam("itemId") Long itemId) {

        Long userId = (Long)request.getAttribute("userId");
        CreateResponse data = bookmarkService.registerBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.REGISTER_BOOKMARK.getMessage(), data));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<ResponseForm> deleteBookmarks(HttpServletRequest request,@PathVariable Long itemId) {
        Long userId = (Long)request.getAttribute("userId");
        DeleteResponse data = bookmarkService.deleteBookmark(userId, itemId);
        return ResponseEntity.ok(
                new ResponseForm(BookmarkResponse.DELETE_BOOKMARK.getMessage(), data));
    }
}

package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ItemDto.ItemRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ItemService;
import com.blackdragon.heytossme.type.resposne.ItemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final String USER_ID = "userId";

    @GetMapping
    public ResponseEntity<ResponseForm> getList(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startDue", required = false) String startDue,
            @RequestParam(name = "endDue", required = false) String endDue,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "category", required = false) String category
    ) {
        var data = itemService.getList(pageNum, size, region, startDue, endDue, searchTitle,
                category);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_ITEM_LIST.getMessage(), data));
    }

    @GetMapping("/{item-id}")
    public ResponseEntity<ResponseForm> getDetail(@PathVariable("item-id") Long itemId) {
        var data = itemService.getDetail(itemId);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_DETAIL.getMessage(), data));
    }

    @PostMapping("/auth")
    public ResponseEntity<ResponseForm> register(HttpServletRequest httpRequest,
            @RequestBody @Valid ItemRequest request) {
        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
//        log.info("request.getSellerId() = {}", sellerId);
        var data = itemService.createItem(sellerId, request);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.REGISTER_ITEM.getMessage(), data));
    }

    @PatchMapping("/{item-id}/auth")
    public ResponseEntity<ResponseForm> modify(HttpServletRequest httpRequest,
            @PathVariable("item-id") Long itemId, @RequestBody ItemRequest request) {
        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
        var data = itemService.modify(itemId, sellerId, request);

        return ResponseEntity.ok(new ResponseForm("abc", data));
    }

    @DeleteMapping("/{item-id}/auth")
    public ResponseEntity<ResponseForm> delete(HttpServletRequest httpRequest,
            @PathVariable("item-id") Long itemId) {
        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
        itemService.deleteItem(itemId, sellerId);

        return ResponseEntity.ok(new ResponseForm("cde", null));
    }
}

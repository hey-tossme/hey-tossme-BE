package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ItemDto.CreateItemRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ItemService;
import com.blackdragon.heytossme.type.ItemResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
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
        var data = itemService.getList(pageNum, size, region, startDue, endDue, searchTitle, category);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_ITEM_LIST.getMessage(), data));
    }

    @PostMapping
    public ResponseEntity<ResponseForm> register(HttpServletRequest httpRequest,
            @RequestBody @Valid CreateItemRequest request) {
        request.setSellerId(Long.valueOf(String.valueOf(httpRequest.getAttribute(USER_ID))));
        var data = itemService.createItem(request);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.REGISTER_ITEM.getMessage(), data));
    }
}

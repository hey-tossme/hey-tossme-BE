package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ItemDto.CreateItemRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ItemService;
import com.blackdragon.heytossme.type.ItemResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ResponseForm> registerItem(@RequestBody CreateItemRequest request) {
        var data = itemService.createItem(request);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.REGISTER_ITEM.getMessage(), data));
    }
}

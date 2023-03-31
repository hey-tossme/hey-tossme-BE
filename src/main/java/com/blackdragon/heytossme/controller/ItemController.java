package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ItemDto.ItemRequest;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.ItemService;
import com.blackdragon.heytossme.type.resposne.ItemResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final ItemService itemService;
    private final String USER_ID = "userId";

    @GetMapping("/v2/items")
    public ResponseEntity<ResponseForm> getList(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "size", required = false) Integer size,
            @RequestParam(name = "region", required = false) String region,
            @RequestParam(name = "startDue", required = false) String startDue,
            @RequestParam(name = "endDue", required = false) String endDue,
            @RequestParam(name = "searchTitle", required = false) String searchTitle,
            @RequestParam(name = "category", required = false) String category
    ) {
        log.info("getItemList start");

        var data = itemService.getList(pageNum, size, region, startDue, endDue, searchTitle,
                category);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_ITEM_LIST.getMessage(), data));
    }

    @GetMapping("/v2/items/{item-id}")
    public ResponseEntity<ResponseForm> getDetail(@PathVariable("item-id") Long itemId) {
        log.info("getItemDetail start");

        var data = itemService.getDetail(itemId);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_DETAIL.getMessage(), data));
    }

    @PostMapping("/v1/items")
    public ResponseEntity<ResponseForm> register(HttpServletRequest httpRequest,
            @RequestBody ItemRequest request) {
        log.info("itemRegister start");

        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
//        log.info("request.getSellerId() = {}", sellerId);
        var data = itemService.createItem(sellerId, request);
        return ResponseEntity.ok(new ResponseForm(ItemResponse.REGISTER_ITEM.getMessage(), data));
    }

    @PatchMapping("/v1/items/{item-id}")
    public ResponseEntity<ResponseForm> modify(HttpServletRequest httpRequest,
            @PathVariable("item-id") Long itemId, @RequestBody ItemRequest request) {
        log.info("itemModify start");

        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
        var data = itemService.modify(itemId, sellerId, request);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.MODIFY_DETAIL.getMessage(), data));
    }

    @DeleteMapping("/v1/items/{item-id}")
    public ResponseEntity<ResponseForm> delete(HttpServletRequest httpRequest,
            @PathVariable("item-id") Long itemId) {
        log.info("itemDelete start");

        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
        itemService.deleteItem(itemId, sellerId);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.DELETE_ITEM.getMessage(), null));
    }

    @PostMapping("/v1/items/{item-id}/transaction-confirm")
    public ResponseEntity<ResponseForm> deal(HttpServletRequest httpRequest,
            @PathVariable("item-id") Long itemId, @RequestParam("buyer-id") Long buyerId) {
        log.info("item deal start");

        Long sellerId = (Long) httpRequest.getAttribute(USER_ID);
        var data = itemService.dealConfirm(itemId, sellerId, buyerId);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.DEAL_CONFIRM.getMessage(), data));
    }

    @GetMapping("/v1/items/complete/buy")
    public ResponseEntity<ResponseForm> checkBuyList(HttpServletRequest httpRequest,
            @PathVariable(name = "page-num", required = false) Integer pageNum,
            @PathVariable(name = "size", required = false) Integer size) {
        log.info("item checkBuyList start");

        Long memberId = (Long) httpRequest.getAttribute(USER_ID);
        var data = itemService.getBuyList(memberId, pageNum, size);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_BUY_LIST.getMessage(), data));
    }

    @GetMapping("/v1/items/complete/sell")
    public ResponseEntity<ResponseForm> checkSellList(HttpServletRequest httpRequest,
            @PathVariable(name = "page-num", required = false) Integer pageNum,
            @PathVariable(name = "size", required = false) Integer size) {
        log.info("item checkSellList start");

        Long memberId = (Long) httpRequest.getAttribute(USER_ID);
        var data = itemService.getSellList(memberId, pageNum, size);

        return ResponseEntity.ok(new ResponseForm(ItemResponse.GET_SELL_LIST.getMessage(), data));
    }

    @GetMapping("/v2/items/address")
    public ResponseEntity<ResponseForm> getAddressList() {
        log.info("item getAddressList start");

        var data = itemService.getAddressList();

        return ResponseEntity.ok(
                new ResponseForm(ItemResponse.GET_ADDRESS_LIST.getMessage(), data));
    }
}

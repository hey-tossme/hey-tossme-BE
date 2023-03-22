package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ItemErrorCode implements BaseErrorCodeImpl {
    ITEM_NOT_FOUND(HttpStatus.NOT_FOUND, "Item not found"),
    ADDRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "Address can't convert to coordinate"),
    SELLER_MISMATCH(HttpStatus.NOT_ACCEPTABLE, "Item seller is not mismatched");

    private final HttpStatus httpStatus;
    private final String message;
}

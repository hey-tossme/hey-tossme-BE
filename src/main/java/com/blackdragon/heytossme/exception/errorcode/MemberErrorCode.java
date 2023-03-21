package com.blackdragon.heytossme.exception.errorcode;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum MemberErrorCode implements BaseErrorCodeImpl {

	MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found"),
	INVALID_KEY(HttpStatus.NOT_ACCEPTABLE, "Invalid token"),
	METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "Expired access token supplied"),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Logout - refresh token expired"),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error");

	private final HttpStatus httpStatus;
	private final String message;
}

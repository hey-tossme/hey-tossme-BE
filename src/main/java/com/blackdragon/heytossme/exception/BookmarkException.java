package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class BookmarkException extends BaseException{
	public BookmarkException(BaseErrorCodeImpl errorCode) {
		super(errorCode);
	}
}

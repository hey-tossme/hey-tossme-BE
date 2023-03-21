package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class KeywordException extends BaseException{
	public KeywordException(BaseErrorCodeImpl errorCode) {
		super(errorCode);
	}
}

package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class MemberException extends BaseException{
	public MemberException(BaseErrorCodeImpl errorCode) {
		super(errorCode);
	}
}

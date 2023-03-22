package com.blackdragon.heytossme.exception;

import com.blackdragon.heytossme.exception.errorcode.impl.BaseErrorCodeImpl;

public class NotificationException extends BaseException{

	public NotificationException(BaseErrorCodeImpl errorCode) {
		super(errorCode);
	}
}

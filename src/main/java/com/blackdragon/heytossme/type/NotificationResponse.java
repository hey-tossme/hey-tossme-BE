package com.blackdragon.heytossme.type;

import lombok.Getter;

@Getter
public enum NotificationResponse {

	GET_NOTIFIACTION_LIST("successfully find notification"),
	NOTIFICATION_STATUS_CHANGE("successfully notification status changed"),
	DELETE_NOTIFIACTION("successfully deleted notification");

	final String message;

	NotificationResponse(String message) {
		this.message = message;
	}

}

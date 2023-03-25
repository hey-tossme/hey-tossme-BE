package com.blackdragon.heytossme.fcm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

	private static final Logger logger = LoggerFactory.getLogger(FcmService.class);
//
//	public void send(final NotificationRequest notificationRequest)
//			throws InterruptedException, ExecutionException {
//		Message message = Message.builder()
//				.setToken(notificationRequest.getToken())
//				.setWebpushConfig(WebpushConfig.builder().putHeader("ttl", "300")
//						.setNotification(new WebpushNotification(notificationRequest.getTitle(),
//								notificationRequest.getMessage()))
//						.build())
//				.build();
//
//		//정상적으로 build가되어 메세지를 보냈다면, response에는 message값(/**/messages/~~~)을 리턴받는다
//		String response = FirebaseMessaging.getInstance().sendAsync(message).get();
//		logger.info("Sent message: " + response);
//	}
}

package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.NotificationDto.NotificationRequest;
import com.blackdragon.heytossme.dto.NotificationDto.Response;
import com.blackdragon.heytossme.exception.NotificationException;
import com.blackdragon.heytossme.exception.errorcode.NotificationErrorCode;
import com.blackdragon.heytossme.persist.NotificationRepository;
import com.blackdragon.heytossme.persist.entity.Notification;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;

    public List<Response> getNotification(Long userId) {
		List<Notification> notificationList = notificationRepository.findAllByMemberId(userId);
		System.out.println(">>>>>>>> notificationList : " + notificationList);
        return notificationList.stream().map(Response::from).collect(Collectors.toList());
    }

    public Response changeStatus(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.UNAUTHORIZED));
        notification.setReadOrNot(true);
        Notification savedNotification = notificationRepository.save(notification);

        return Response.from(savedNotification);
    }

    public Response deleteNotification(Long notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationException(NotificationErrorCode.UNAUTHORIZED));

		notificationRepository.deleteById(notification.getId());
		return Response.from(notification);
	}

	public void initializer() {
		try {
			FileInputStream inputStream =
					new FileInputStream("src/main/resources/firebase-service-account.json");

			FirebaseOptions options = FirebaseOptions.builder()
					.setCredentials(GoogleCredentials.fromStream(inputStream))
					.build();
			FirebaseApp.initializeApp(options);
		} catch (IOException e) {
			log.error("Failed load FCM file");
		} catch (Exception e) {
			System.out.println(">>>>>>>>>> 초기화 에러" + e.getMessage());
		}
	}

	public void sendPush(NotificationRequest request) {

		WebpushConfig webpushConfig = WebpushConfig.builder()
				.setNotification(new WebpushNotification(request.getTitle(), request.getBody()))
				.build();

		Message message = Message.builder()
				.setWebpushConfig(webpushConfig)
				.setToken(request.getRegistrationToken())
				.build();

		try {
			String response = FirebaseMessaging.getInstance().send(message);
			log.info("response : " + response);
		} catch (FirebaseMessagingException e) {
			log.error("fcm을 통한 메시지 push 발송 실패");
			log.error("error code : " + e.getErrorCode());
			log.error("error message : " + e.getMessage());
		}

		Notification notification = Notification.builder()
				.message(request.getBody())
				.type(request.getType())
				.readOrNot(false)
				.item(request.getItem())
				.member(request.getMember())
				.build();

		notificationRepository.save(notification);
	}
}

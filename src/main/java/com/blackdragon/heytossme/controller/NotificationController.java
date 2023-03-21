package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.NotificationDto;
import com.blackdragon.heytossme.dto.NotificationDto.Response;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.NotificationService;
import com.blackdragon.heytossme.type.NotificationResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/notification")
@RequiredArgsConstructor
@RestController
public class NotificationController {

	private final NotificationService notificationService;
//	private final FCMService fcmService;

	//테스트용(알람저장) TODO
	@PostMapping("/auth")
	public ResponseEntity<ResponseForm> saveNotification(HttpServletRequest request) {

		Long userId = (Long) request.getAttribute("userId");
		Response data = notificationService.saveNoti();
		return ResponseEntity.ok(
				new ResponseForm(NotificationResponse.GET_NOTIFIACTION_LIST.getMessage(), data));
	}

	@GetMapping("/auth")
	public ResponseEntity<ResponseForm> getNotification(HttpServletRequest request) {
		Long userId = (Long) request.getAttribute("userId");
		List<NotificationDto.Response> data = notificationService.getNotification();
		return ResponseEntity.ok(
				new ResponseForm(NotificationResponse.GET_NOTIFIACTION_LIST.getMessage(), data));
	}

	@PatchMapping("/{notification-id}/auth")
	public ResponseEntity<ResponseForm> statusChangeNotification(HttpServletRequest request,
										@PathVariable("notification-id") Long notificationId) {

//		Long userId = (Long) request.getAttribute("userId");
		String token = (String) request.getAttribute("accessToken");
		Response data = notificationService.changeStatus(notificationId);

		return ResponseEntity.ok(
		new ResponseForm(NotificationResponse.NOTIFICATION_STATUS_CHANGE.getMessage(), data, token));
	}

	@DeleteMapping("/{notification-id}/auth")
	public ResponseEntity<ResponseForm> deleteNotification(HttpServletRequest request,
														@PathVariable("notification-id") Long id) {

		Response data = notificationService.deleteNotification(id);

		return ResponseEntity.ok(
				new ResponseForm(NotificationResponse.DELETE_NOTIFIACTION.getMessage(), data)
		);
	}

//	@PostMapping("/send-notification")
//	public void sendNotification(@RequestParam("token") String token,
//								@RequestParam("title") String title,
//								@RequestParam("body") String body) throws FirebaseMessagingException{
//
//		fcmService.sendNotification(token, title, body);
//
//	}
}

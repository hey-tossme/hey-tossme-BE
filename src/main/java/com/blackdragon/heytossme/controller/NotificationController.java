package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.NotificationDto;
import com.blackdragon.heytossme.dto.NotificationDto.Response;
import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.service.NotificationService;
import com.blackdragon.heytossme.type.NotificationResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v1/notification")
@RequiredArgsConstructor
@RestController
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    //	private final FCMService fcmService;
    private static final String USER_ID = "userId";
    private static final String ACCESS_TOKEN = "accessToken";

    @GetMapping
    public ResponseEntity<ResponseForm> getNotification() {
        log.info("getNotification start");

        List<NotificationDto.Response> data = notificationService.getNotification();
        return ResponseEntity.ok(
                new ResponseForm(NotificationResponse.GET_NOTIFIACTION_LIST.getMessage(), data));
    }

    @PatchMapping("/{notification-id}")
    public ResponseEntity<ResponseForm> statusChangeNotification(HttpServletRequest request,
            @PathVariable("notification-id") Long notificationId) {
        log.info("statusChangeNotification start");

        String token = (String) request.getAttribute(ACCESS_TOKEN);
        Response data = notificationService.changeStatus(notificationId);

        return ResponseEntity.ok(
                new ResponseForm(NotificationResponse.NOTIFICATION_STATUS_CHANGE.getMessage(), data,
                        token));
    }

    @DeleteMapping("/{notification-id}")
    public ResponseEntity<ResponseForm> deleteNotification(HttpServletRequest request,
            @PathVariable("notification-id") Long id) {
        log.info("deleteNotification start");

        Response data = notificationService.deleteNotification(id);

        return ResponseEntity.ok(
                new ResponseForm(NotificationResponse.DELETE_NOTIFIACTION.getMessage(), data)
        );
    }
}

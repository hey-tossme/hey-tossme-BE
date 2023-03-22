package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.NotificationDto.Response;
import com.blackdragon.heytossme.exception.NotificationException;
import com.blackdragon.heytossme.exception.errorcode.NotificationErrorCode;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.NotificationRepository;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.persist.entity.Notification;
import com.blackdragon.heytossme.type.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class NotificationService {

	private final NotificationRepository notificationRepository;
	private final MemberRepository memberRepository;	//TODO
	private final ItemRepository itemRepository; 	//TODO
	private final Map<Long, String> tokenMap = new HashMap<>();

	//테스트용
	public Response saveNoti() {

		Item item = itemRepository.findById(1L).get();
		Member member = memberRepository.findById(1L).get();

		Notification notification = Notification.builder()
				.message("임영웅 콘서트 티켓이 업로드되었습니다")
				.readOrNot(false)
				.type(Type.KEYWORD.getToKorean())
				.item(item)
				.member(member)
				.build();
		Notification save = notificationRepository.save(notification);
		return Response.from(save);
	}

	public List<Response> getNotification() {
		List<Notification> notificationList = notificationRepository.findAll();

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

	//로그인 후 토큰을 서버에서 관리
	public void register(final Long userId, final String token) {
		tokenMap.put(userId, token);
	}

	//로그아웃 되면 토큰삭제
	public void deleteToken(final Long userId) {
		tokenMap.clear();
	}

}

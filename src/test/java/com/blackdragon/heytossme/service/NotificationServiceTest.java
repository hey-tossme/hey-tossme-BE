//package com.blackdragon.heytossme.service;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.BDDMockito.given;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
//
//import com.blackdragon.heytossme.dto.NotificationDto.Response;
//import com.blackdragon.heytossme.persist.NotificationRepository;
//import com.blackdragon.heytossme.persist.entity.Item;
//import com.blackdragon.heytossme.persist.entity.Notification;
//import com.blackdragon.heytossme.type.Category;
//import com.blackdragon.heytossme.type.NotificationType;
//import java.time.LocalDateTime;
//import java.util.Optional;
//import org.assertj.core.api.Assertions;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class NotificationServiceTest {
//
//	@Mock
//	private NotificationRepository notificationRepository;
//
//	@InjectMocks
//	private NotificationService notificationService;
//
//	@Test
//	@DisplayName("알림 상태 변경확인")
//	void notificationStatusChange() {
//	  //given
//		Item item = Item.builder()
//				.id(1L)
//				.category(Category.CONCERT)
//				.title("임영웅 취소표")
//				.contents("취소표 팔아요")
//				.price(1000)
//				.dueDate(LocalDateTime.now())
//				.latitude(1)
//				.longitude(2)
//				.imageUrl("thisisimageurl")
//				.status("판매중")
//				.build();
//
//		Notification noti = Notification.builder()
//				.id(1L)
//				.message("임영웅 콘서트표가 등록되었습니다")
//				.type(NotificationType.BOOKMARK)
//				.readOrNot(false)
//				.item(item)
//				.build();
//		given(notificationRepository.findById(anyLong()))
//				.willReturn(Optional.ofNullable(noti));
//		given(notificationRepository.save(any()))
//				.willReturn(noti);
//
//	  //when
//		Response response = notificationService.changeStatus(233L);
//
//		//then
//		assertTrue(response.isReadOrNot());
//		verify(notificationRepository, times(1)).findById(anyLong());
//		verify(notificationRepository, times(1)).save(any());
//	}
//
//	@Test
//	@DisplayName("알림 삭제")
//	void deleteNotification() throws Exception {
//	  //given
//		Item item = Item.builder()
//				.id(1L)
//				.category(Category.CONCERT)
//				.title("임영웅 취소표")
//				.contents("취소표 팔아요")
//				.price(1000)
//				.dueDate(LocalDateTime.now())
//				.latitude(1)
//				.longitude(2)
//				.imageUrl("thisisimageurl")
//				.status("판매중")
//				.build();
//
//		Notification notification = Notification.builder()
//				.id(1L)
//				.message("임영웅 키워드가 삭제되었습니다")
//				.type(NotificationType.KEYWORD)
//				.readOrNot(true)
//				.item(item)
//				.build();
//
//		given(notificationRepository.findById(anyLong()))
//				.willReturn(Optional.ofNullable(notification));
//
//	  //when
//		notificationService.deleteNotification(notification.getId());
//
//	  //then
//		Assertions.assertThat(notificationRepository.findById(1L).isEmpty());
//		verify(notificationRepository, times(1)).deleteById(anyLong());
//	}
//}
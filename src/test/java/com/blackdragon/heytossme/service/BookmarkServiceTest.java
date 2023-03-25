package com.blackdragon.heytossme.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.persist.BookmarkRepository;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Bookmark;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.service.BookmarkService;
import com.blackdragon.heytossme.type.Category;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookmarkServiceTest {

	@Mock
	private BookmarkRepository bookmarkRepository;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private ItemRepository itemRepository;

	@InjectMocks
	private BookmarkService bookmarkService;


	@Test
	@DisplayName("북마크 조회")
	void getAllBookmarks() throws Exception {
		Pageable pageable = Pageable.ofSize(20);

		//given
		given(bookmarkRepository.findAllByMemberId(anyLong(), pageable))
				.willReturn(Page.empty());

		//when
		Page<CreateResponse> bookmarks =
				bookmarkService.getBookmarkList(11L, 0, 8);
		//then
		assertThat(bookmarks.isEmpty());

	}

	@Test
	@DisplayName("북마크 등록")
	void addBookmark() throws Exception {

		Member member = Member.builder()
				.id(1L)
				.email("bomilee@gmail.com")
				.name("bomi")
				.password("bomi!!")
				.pwAuthKey("authkey")
				.imageUrl("sss")
				.socialLoginType("login")
				.status("정상")
				.account("1111-111")
				.bankName("kakao")
				.build();

		Item item = Item.builder()
				.id(1L)
				.category(Category.CONCERT)
				.title("임영웅 취소표")
				.contents("취소표 팔아요")
				.price(1000)
				.dueDate(LocalDateTime.now())
				.latitude(1)
				.longitude(2)
				.imageUrl("thisisimageurl")
				.status("판매중")
				.build();
		Bookmark bookmark = Bookmark.builder()
				.id(1L)
				.item(item)
				.member(member)
				.build();
		//given
		given(itemRepository.findById(anyLong()))
				.willReturn(Optional.of(item));
		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));
		given(bookmarkRepository.save(any()))
				.willReturn(bookmark);

		//when
		CreateResponse createResponse = bookmarkService.registerBookmark(233L, 23L);

		//then
		assertEquals("임영웅 취소표", createResponse.getTitle());
		assertEquals("판매중", createResponse.getStatus());
		assertEquals(1L, createResponse.getUserId());
		verify(bookmarkRepository, times(1)).save(any());
	}

	@Test
	@DisplayName("북마크 삭제")
	void deleteBookmarks() throws Exception {
		Member member = Member.builder()
				.id(1L)
				.email("bomilee@gmail.com")
				.name("bomi")
				.password("bomi!!")
				.pwAuthKey("authkey")
				.imageUrl("sss")
				.socialLoginType("login")
				.status("정상")
				.account("1111-111")
				.bankName("kakao")
				.build();

		Item item = Item.builder()
				.id(1L)
				.category(Category.CONCERT)
				.title("임영웅 취소표")
				.contents("취소표 팔아요")
				.price(1000)
				.dueDate(LocalDateTime.now())
				.latitude(1)
				.longitude(2)
				.imageUrl("thisisimageurl")
				.status("판매중")
				.build();

		Bookmark bookmark = Bookmark.builder()
				.id(1L)
				.item(item)
				.member(member)
				.build();
		//given
		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));
		given(bookmarkRepository.findById(anyLong()))
				.willReturn(Optional.of(bookmark));

		//when
		DeleteResponse deleted = bookmarkService.deleteBookmark(2L, 2L);

		//then
		assertEquals("취소표 팔아요", deleted.getContents());
		verify(bookmarkRepository, times(1)).deleteById(anyLong());
	}
}
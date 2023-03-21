package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.BookmarkDto;
import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.exception.BookmarkException;
import com.blackdragon.heytossme.exception.ItemException;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.BookmarkErrorCode;
import com.blackdragon.heytossme.exception.errorcode.ItemErrorCode;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import com.blackdragon.heytossme.persist.BookmarkRepository;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Bookmark;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	public Page<CreateResponse> getBookmarkList(Long userId, Integer pageNum, Integer size) {
		Pageable pageable = PageRequest.of(pageNum == null ? 0 : pageNum, size);
		Page<Bookmark> page = bookmarkRepository.findAllByMemberId(userId, pageable);
		return page.map(CreateResponse::from);
	}

	public CreateResponse registerBookmark(Long userId, Long itemId) {
		Item item = itemRepository.findById(itemId)
						.orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
		Member member = memberRepository.findById(userId)
						.orElseThrow(() -> new MemberException(MemberErrorCode.UNAUTHORIZED));
		Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
								.item(item)
								.member(member)
								.build());

		return BookmarkDto.CreateResponse.from(bookmark);
	}

	public DeleteResponse deleteBookmark(Long userId, Long itemId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
		Bookmark bookmark = bookmarkRepository.findById(itemId)
				.orElseThrow(() -> new BookmarkException(BookmarkErrorCode.UNAUTHORIZED));

		bookmarkRepository.deleteById(bookmark.getId());
		return DeleteResponse.from(bookmark);
	}
}

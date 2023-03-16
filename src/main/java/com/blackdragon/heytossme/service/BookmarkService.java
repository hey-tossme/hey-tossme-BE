package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.BookmarkDto;
import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import com.blackdragon.heytossme.persist.BookmarkRepository;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Bookmark;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.BookmarkResponse;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {

	private final BookmarkRepository bookmarkRepository;
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	public List<CreateResponse> getBookmarkList(Long userId) {
		List<Bookmark> bookmarkList = bookmarkRepository.findAllById(Collections.singleton(userId));
		return bookmarkList.stream()
				.map(e -> BookmarkDto.CreateResponse.from(e)).collect(Collectors.toList());
	}

	public CreateResponse registerBookmark(Long userId, Long itemId) {
		Item item = itemRepository.findById(itemId)
						.orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_PARAMETER));
		Member member = memberRepository.findById(userId)
						.orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_PARAMETER));
		Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
								.item(item)
								.member(member)
								.build());
//		return new CreateResponse(bookmark);
		return BookmarkDto.CreateResponse.from(bookmark);
	}

	public DeleteResponse deleteBookmark(Long userId, Long itemId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.INVALID_TOKEN));
		Bookmark bookmark = bookmarkRepository.findById(itemId)
				.orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_PARAMETER));

		bookmarkRepository.deleteById(bookmark.getId());
		return DeleteResponse.from(bookmark);
	}
}

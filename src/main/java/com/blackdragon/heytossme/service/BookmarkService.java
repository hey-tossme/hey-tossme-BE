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
import java.util.List;
import java.util.stream.Collectors;
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

	public List<CreateResponse> getBookmarkList(Long userId, Integer pageNum, Integer size) {
		Pageable pageable = PageRequest.of(pageNum == null ? 0 : pageNum, size);
		Page<Bookmark> page = bookmarkRepository.findAllByMemberId(userId, pageable);

		return page.stream().map(CreateResponse::from).collect(Collectors.toList());
	}

	public CreateResponse registerBookmark(Long userId, Long itemId) {
		Item item = itemRepository.findById(itemId)
						.orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
		Member member = memberRepository.findById(userId)
						.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
								.item(item)
								.member(member)
								.build());
//		return new CreateResponse(bookmark);
		return BookmarkDto.CreateResponse.from(bookmark);
	}

	public DeleteResponse deleteBookmark(Long userId, Long itemId) {
		Member member = memberRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND));
		Bookmark bookmark = bookmarkRepository.findById(itemId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		bookmarkRepository.deleteById(bookmark.getId());
		return DeleteResponse.from(bookmark);
	}
}

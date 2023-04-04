package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.config.WebConfig;
import com.blackdragon.heytossme.dto.BookmarkDto;
import com.blackdragon.heytossme.dto.BookmarkDto.CreateResponse;
import com.blackdragon.heytossme.dto.BookmarkDto.DeleteResponse;
import com.blackdragon.heytossme.dto.NotificationDto.NotificationRequest;
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
import com.blackdragon.heytossme.type.NotificationType;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;
    private final NotificationService notificationService;
    private final WebConfig webConfig;

    public Page<CreateResponse> getBookmarkList(Long userId, Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum == null ? 0 : pageNum, size);
        Page<Bookmark> page = bookmarkRepository.findAllByMemberId(userId, pageable);
        return page.map(CreateResponse::from);
    }

    public CreateResponse registerBookmark(Long userId, Long itemId) {
        boolean existedBookmark = bookmarkRepository.findByItemIdAndMemberId(itemId, userId).isEmpty();
        if (!existedBookmark) {
            throw new BookmarkException(BookmarkErrorCode.DUPLICATED);
        }

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND));
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.UNAUTHORIZED));
        Bookmark bookmark = bookmarkRepository.save(Bookmark.builder()
                .item(item)
                .member(member)
                .build());

        NotificationRequest notificationInfo = NotificationRequest.builder()
                .registrationToken(item.getSeller().getRegistrationToken())
                .title("BOOKMARK")
                .body(item.getSeller().getName() + "고객님의 "
                        + item.getTitle() + "제품이 북마크 처리되었습니다")
                .type(NotificationType.BOOKMARK)
                .item(item)
                .member(member)
                .build();
        log.info(">>>>> 북마크 된 상품의 판매자 토큰 : "+  notificationInfo.getRegistrationToken());
        notificationService.sendPush(notificationInfo, webConfig.initializer());

        return BookmarkDto.CreateResponse.from(bookmark);
    }

    public DeleteResponse deleteBookmark(Long userId, Long itemId) {
        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
        Bookmark bookmark = bookmarkRepository.findByItemIdAndMemberId(itemId, member.getId())
                .orElseThrow(() -> new BookmarkException(BookmarkErrorCode.UNAUTHORIZED));
        bookmark.getMember().setId(member.getId());
        bookmarkRepository.deleteById(bookmark.getId());
        return DeleteResponse.from(bookmark);
    }
}

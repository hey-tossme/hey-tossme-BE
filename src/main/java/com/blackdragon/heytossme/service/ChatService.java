package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ChatRoomDto;
import com.blackdragon.heytossme.persist.ChatMessageRepository;
import com.blackdragon.heytossme.persist.ChatRoomRepository;
import com.blackdragon.heytossme.persist.entity.ChatRoom;
import com.blackdragon.heytossme.persist.entity.Item;
import com.blackdragon.heytossme.persist.entity.Member;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
//    private final ItemRepository itemRepository;
//    private final MemberRepository memberRepository;-

    public List<ChatRoomDto.Response> getChatRoomList(Long userId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllBySellerId(userId);
        chatRoomList.addAll(chatRoomRepository.findAllByBuyerId(userId));
        Collections.sort(chatRoomList);

        return chatRoomList.stream().map(ChatRoomDto.Response::new).collect(Collectors.toList());
    }

    public ChatRoomDto.Response createChatRoom(Long buyerId, Long sellerId, Long itemId) {
//        Member buyer = memberRepository.findById(buyerId).orElseThrow(() -> new CustomException(
//                ErrorCode.INSUFFICIENT_PARAMETER));
//        Member seller = memberRepository.findById(buyerId).orElseThrow(() -> new CustomException(
//                ErrorCode.INSUFFICIENT_PARAMETER));
        Member buyer = Member.builder()
                .id(buyerId)
                .email("qwer@naver.com")
                .name("김도개자")
                .password("123")
                .createdAt(LocalDateTime.now())
                .status("정상 회원")
                .build();
        Member seller = Member.builder()
                .id(sellerId)
                .email("qwere@naver.com")
                .name("김도개자2")
                .password("1234")
                .createdAt(LocalDateTime.now().minusDays(1))
                .status("정상 회원")
                .build();
        Item item = Item.builder()
                .id(itemId)
                .member(seller)
                .category("concert")
                .title("SASA")
                .contents("SADASADA")
                .price(5000)
                .createdAt(LocalDateTime.now().minusDays(1))
                .status("판매중")
                .build();
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .buyer(buyer)
                .seller(seller)
                .item(item)
                .accountTransferStatus(false)
                .build());
        return new ChatRoomDto.Response(chatRoom);
    }

}

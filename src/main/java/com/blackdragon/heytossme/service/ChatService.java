package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ChatRoomDto;
import com.blackdragon.heytossme.persist.ChatMessageRepository;
import com.blackdragon.heytossme.persist.ChatRoomRepository;
import com.blackdragon.heytossme.persist.entity.ChatRoom;
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
//        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
//                .buyer(buyer)
//                .seller(seller)
//                .item(item)
//                .accountTransferStatus(false)
//                .build());
//        return new ChatRoomDto.Response(chatRoom);
        return null;
    }

}

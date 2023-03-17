package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ChatRoomDto;
import com.blackdragon.heytossme.dto.ChatRoomDto.Response;
import com.blackdragon.heytossme.dto.MessageDto;
import com.blackdragon.heytossme.dto.MessageDto.SendMessage;
import com.blackdragon.heytossme.exception.AuthException;
import com.blackdragon.heytossme.exception.ChatException;
import com.blackdragon.heytossme.exception.ItemException;
import com.blackdragon.heytossme.exception.errorcode.AuthErrorCode;
import com.blackdragon.heytossme.exception.errorcode.ChatErrorCode;
import com.blackdragon.heytossme.exception.errorcode.ItemErrorCode;
import com.blackdragon.heytossme.persist.ChatMessageRepository;
import com.blackdragon.heytossme.persist.ChatRoomRepository;
import com.blackdragon.heytossme.persist.ItemRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.ChatMessage;
import com.blackdragon.heytossme.persist.entity.ChatRoom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoomDto.Response> getChatRoomList(Long userId) {
        List<ChatRoom> chatRoomList = chatRoomRepository.findAllBySellerId(userId);
        chatRoomList.addAll(chatRoomRepository.findAllByBuyerId(userId));
        Collections.sort(chatRoomList);
        log.info("chatRoomList = {}", chatRoomList);

        return chatRoomList.stream().map(ChatRoomDto.Response::new).collect(Collectors.toList());
    }

    public ChatRoomDto.Response createChatRoom(Long userId, Long itemId) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .buyer(memberRepository.findById(userId)
                        .orElseThrow(() -> new AuthException(
                                AuthErrorCode.USER_NOT_FOUND
                        )))
                .seller(memberRepository.findById(itemRepository.findById(itemId)
                                .orElseThrow(() -> new ItemException(ItemErrorCode.ITEM_NOT_FOUND))
                                .getMember().getId())
                        .orElseThrow(() -> new AuthException(
                                AuthErrorCode.USER_NOT_FOUND)
                        ))
                .item(itemRepository.findById(itemId)
                        .orElseThrow(() -> new ItemException(
                                ItemErrorCode.ITEM_NOT_FOUND
                        )))
                .accountTransferStatus(false)
                .build());
//        log.info("chatRoom.getChatMessageList() = {}", chatRoom.getChatMessageList());
        return new ChatRoomDto.Response(chatRoom);
    }

    public void sendMessage(SendMessage request) {
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoomRepository.findById(request.getChatRoomId())
                        .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND)))
                .sender(memberRepository.findById(request.getSenderId())
                        .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND)))
                .message(request.getMessage())
                .build());
    }

    public List<MessageDto.Response> getChatRoomMessage(Long roomId, Long userId) {

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!(chatRoom.getBuyer().getId().equals(userId) || chatRoom.getSeller().getId()
                .equals(userId))) {
            throw new ChatException(ChatErrorCode.NOT_ACCEPTABLE_USER);
        }

        List<ChatMessage> chatRoomList = chatMessageRepository.findAllByChatRoomId(roomId);

        return chatRoomList.stream().map(MessageDto.Response::new).collect(Collectors.toList());
    }

    public ChatRoomDto.Response convertAccountTransferStatus(Long userId, Long roomId,
            boolean status) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!chatRoom.getSeller().getId().equals(userId)) {
            throw new ChatException(ChatErrorCode.USER_MISMATCH_TO_SELLER);
        }

        chatRoom.setAccountTransferStatus(status);

        chatRoom = chatRoomRepository.save(chatRoom);

        return new Response(chatRoom);
    }

    public void deleteChatRoom(Long userId, Long roomId) {

        if (!chatRoomRepository.existsById(roomId)) {
            throw new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND);
        }

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ChatException(ChatErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!(chatRoom.getBuyer().getId().equals(userId)
                || chatRoom.getSeller().getId().equals(userId))) {
            throw new ChatException(ChatErrorCode.NOT_ACCEPTABLE_USER);
        }

        chatRoomRepository.deleteById(roomId);
    }
}

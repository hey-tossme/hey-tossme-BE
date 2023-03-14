package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.ChatRoomDto;
import com.blackdragon.heytossme.dto.ChatRoomDto.ConvertAccountStatusRequest;
import com.blackdragon.heytossme.dto.ChatRoomDto.CreateRequest;
import com.blackdragon.heytossme.dto.ChatRoomDto.Response;
import com.blackdragon.heytossme.dto.MessageDto;
import com.blackdragon.heytossme.dto.MessageDto.SendMessage;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
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

    public ChatRoomDto.Response createChatRoom(CreateRequest request) {
        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.builder()
                .buyer(memberRepository.findById(request.getBuyerId())
                        .orElseThrow(() -> new CustomException(
                                ErrorCode.USER_NOT_FOUND
                        )))
                .seller(memberRepository.findById(itemRepository.findById(request.getItemId())
                                .orElseThrow(() -> new CustomException(ErrorCode.ITEM_NOT_FOUND))
                                .getMember().getId())
                        .orElseThrow(() -> new CustomException(
                                ErrorCode.USER_NOT_FOUND)
                        ))
                .item(itemRepository.findById(request.getItemId())
                        .orElseThrow(() -> new CustomException(
                                ErrorCode.ITEM_NOT_FOUND
                        )))
                .accountTransferStatus(false)
                .build());
//        log.info("chatRoom.getChatMessageList() = {}", chatRoom.getChatMessageList());
        return new ChatRoomDto.Response(chatRoom);
    }

    public void sendMessage(SendMessage request) {
        chatMessageRepository.save(ChatMessage.builder()
                .chatRoom(chatRoomRepository.findById(request.getChatRoomId())
                        .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND)))
                .sender(memberRepository.findById(request.getSenderId())
                        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)))
                .message(request.getMessage())
                .build());
    }

    public List<MessageDto.Response> getChatRoomMessage(Long roomId) {
        List<ChatMessage> chatRoomList = chatMessageRepository.findAllByChatRoomId(roomId);

        return chatRoomList.stream().map(MessageDto.Response::new).collect(Collectors.toList());
    }

    public ChatRoomDto.Response convertAccountTransferStatus(ConvertAccountStatusRequest request) {
        ChatRoom chatRoom = chatRoomRepository.findById(request.getChatRoodId())
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        chatRoom.setAccountTransferStatus(request.getAccountTransferStatus());

        chatRoom = chatRoomRepository.save(chatRoom);

        return new Response(chatRoom);
    }
}

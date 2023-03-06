package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.persist.ChatMessageRepository;
import com.blackdragon.heytossme.persist.ChatRoomRepository;
import com.blackdragon.heytossme.persist.entity.ChatMessage;
import com.blackdragon.heytossme.persist.entity.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatService implements ChatServiceImpl{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Override
    public List<ChatRoom> getChatRoomList(Long userId) {
        return null;
    }

    @Override
    public ChatRoom createChatRoom(ChatRoom chatRoom) {
        return null;
    }

    @Override
    public ChatRoom quitChatRoom(ChatRoom chatRoom, Long userId) {
        return null;
    }

    @Override
    public List<ChatMessage> getChatMessageList(ChatRoom ChatRoom) {
        return null;
    }

    @Override
    public void createMessage(ChatMessage chatMessage) {

    }
}

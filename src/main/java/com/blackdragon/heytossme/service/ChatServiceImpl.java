package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.persist.entity.ChatMessage;
import com.blackdragon.heytossme.persist.entity.ChatRoom;

import java.util.List;

public interface ChatServiceImpl {

    List<ChatRoom> getChatRoomList(Long userId);

    ChatRoom createChatRoom(ChatRoom chatRoom);

    ChatRoom quitChatRoom(ChatRoom chatRoom, Long userId);

    List<ChatMessage> getChatMessageList(ChatRoom ChatRoom);

    void createMessage(ChatMessage chatMessage);


}

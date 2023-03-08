package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    List<ChatRoom> findAllBySellerId(Long sellerId);

    List<ChatRoom> findAllByBuyerId(Long BuyerId);
}

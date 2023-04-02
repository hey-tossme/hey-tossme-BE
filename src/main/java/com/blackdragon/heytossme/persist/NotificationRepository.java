package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Notification;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

	List<Notification> findAllByMemberId(Long memberId);
}

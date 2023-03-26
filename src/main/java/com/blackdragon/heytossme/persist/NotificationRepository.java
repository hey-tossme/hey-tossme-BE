package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Notification1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification1, Long> {

}

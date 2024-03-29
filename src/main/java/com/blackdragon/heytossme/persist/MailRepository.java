package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Mail, String> {

}

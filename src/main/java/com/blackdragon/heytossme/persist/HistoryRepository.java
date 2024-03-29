package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<History, Long> {

    Page<History> findAllByBuyerId(Long buyerId, Pageable pageable);
}

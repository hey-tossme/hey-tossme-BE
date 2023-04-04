package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Bookmark;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    Page<Bookmark> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Bookmark> findByItemIdAndMemberId(Long itemId, Long memberId);
}

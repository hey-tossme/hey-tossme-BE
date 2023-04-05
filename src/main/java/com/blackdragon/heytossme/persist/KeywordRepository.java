package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Keyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

    List<Keyword> findAllByMemberId(Long memberId);

    Keyword findByKeywordAndMemberId(String keyword, Long memberId);

    @Query("select k from Keyword k where :title like concat('%', k.keyword, '%')")
    List<Keyword> findKeyword(@Param("title") String title);
}

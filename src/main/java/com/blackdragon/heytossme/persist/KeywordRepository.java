package com.blackdragon.heytossme.persist;

import com.blackdragon.heytossme.persist.entity.Keyword;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {

	List<Keyword> findAllByMemberId(Long memberId);

	Keyword findByKeyword(String keyword);
}

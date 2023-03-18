package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.dto.KeywordDto.Response;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import com.blackdragon.heytossme.persist.KeywordRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Keyword;
import com.blackdragon.heytossme.persist.entity.Member;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KeywordService {

	private final KeywordRepository keywordRepository;
	private final MemberRepository memberRepository;

	public Keyword getKeyword(String keyword) {
		return keywordRepository.findByKeyword(keyword);
	}

	public List<Response> getKeywordList(Long memberId) {
		List<Keyword> keywordList = keywordRepository.findAllByMemberId(memberId);
		return keywordList.stream().map(Response::from).collect(Collectors.toList());
	}

	public Response registerKeyword(Long userId, String keyword) {

		Member member = memberRepository.findById(userId)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

		Keyword savedKeyword = keywordRepository.save(Keyword.builder()
				.keyword(keyword)
				.member(member)
				.build());

		return Response.from(savedKeyword);
	}

	public Response deleteKeyword(String keyword) {
		Keyword savedKeyword = keywordRepository.findByKeyword(keyword);
		keywordRepository.deleteById(savedKeyword.getId());
		return Response.from(savedKeyword);
	}
}

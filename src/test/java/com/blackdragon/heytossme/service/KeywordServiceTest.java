package com.blackdragon.heytossme.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.blackdragon.heytossme.dto.KeywordDto.Response;
import com.blackdragon.heytossme.persist.KeywordRepository;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Keyword;
import com.blackdragon.heytossme.persist.entity.Member;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KeywordServiceTest {

	@Mock
	private KeywordRepository keywordRepository;
	@Mock
	private MemberRepository memberRepository;

	@InjectMocks
	private KeywordService keywordService;

//	@Test
//	@DisplayName("키워드 조회")
//	void getKeyword() throws Exception {
//		//given
//
//		Member member = Member.builder()
//				.id(1L)
//				.email("bomilee.dev@gmail.com")
//				.name("이보미")
//				.password("bombom11!")
//				.status("정상")
//				.build();
//
//		Keyword keyword1 = Keyword.builder()
//				.id(1L)
//				.keyword("숙박")
//				.member(member)
//				.build();
//		Keyword keyword2 = Keyword.builder()
//				.id(2L)
//				.keyword("미용")
//				.member(member)
//				.build();
//		keywordRepository.save(keyword1);
//		keywordRepository.save(keyword2);
//		given(memberRepository.findById(anyLong()))
//				.willReturn(Optional.ofNullable(member));
//		given(keywordRepository.findAllByMemberId(any()))
//				.willReturn((List<Keyword>) keyword1);
//
//		//when
//		List<Response> keywordList = keywordService.getKeywordList(22L);
//
//		//then
//		assertEquals("숙박", keywordList.get(0).getKeyword());
//
//	}

	@Test
	@DisplayName("키워드등록")
	void registerKeyword(){
	  	//given
		Member member = Member.builder()
		.id(1L)
		.email("bomilee.dev@gmail.com")
		.name("이보미")
		.password("bombom11!")
		.status("정상")
		.build();

		given(memberRepository.findById(anyLong()))
				.willReturn(Optional.of(member));

		given(keywordRepository.save(any()))
				.willReturn(Keyword.builder()
						.keyword("숙박")
						.member(member)
						.build());

	  	//when
		Response keyword = keywordService.registerKeyword(1L, "숙박");

		//then
		Assertions.assertEquals("숙박", keyword.getKeyword());
	}

	@Test
	@DisplayName("키워드삭제")
	void deleteKeyword(){
	  //given
		Member member = Member.builder()
				.id(1L)
				.email("bomilee.dev@gmail.com")
				.name("이보미")
				.password("bombom11!")
				.status("정상")
				.build();

		given(keywordService.getKeyword(anyString()))
				.willReturn(Keyword.builder()
						.keyword("salon")
						.member(member)
						.build());

	  //when
		Response data = keywordService.deleteKeyword("salonssss");

		//then
		Assertions.assertEquals("salon", data.getKeyword());
	}
}
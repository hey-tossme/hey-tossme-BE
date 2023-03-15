package com.blackdragon.heytossme.service;

import static com.blackdragon.heytossme.exception.MemberErrorCode.INCORRECT_PASSWORD;
import static com.blackdragon.heytossme.exception.MemberErrorCode.MATCH_PREVIOUS_PASSWORD;
import static com.blackdragon.heytossme.exception.MemberErrorCode.NOT_FOUND_USER;

import com.blackdragon.heytossme.dto.MemberDto;
import com.blackdragon.heytossme.dto.MemberDto.ModifyRequest;
import com.blackdragon.heytossme.dto.MemberDto.Response;
import com.blackdragon.heytossme.dto.MemberDto.SignInRequest;
import com.blackdragon.heytossme.dto.MemberDto.SignUpRequest;
import com.blackdragon.heytossme.exception.CustomException;
import com.blackdragon.heytossme.exception.ErrorCode;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import com.blackdragon.heytossme.type.MemberStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper modelMapper;

    public MemberDto.Response signUp(SignUpRequest request) {

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.CONFLICT_EMAIL);
        }

        Member member = memberRepository.save(
                Member.builder()
                        .email(request.getEmail())
                        .name(request.getName())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .imageUrl(request.getImageUrl())
                        .socialLoginType(request.getSocialType())
                        .status(MemberStatus.NORMAL.name())
                        .build()
        );
        return new Response(member);
    }


    public Member signIn(SignInRequest request) {

        Member member = memberRepository.findByEmail(request.getEmail());

        if (member == null) {
            throw new MemberException(NOT_FOUND_USER);
        }

        if (passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new MemberException(INCORRECT_PASSWORD);
        }

        return member;
    }

    public Response getInfo(long userId) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_USER));

        Response response = modelMapper.map(member, Response.class);

        return response;
    }

    public Response modifyInfo(long userId, ModifyRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_USER));

        log.info(passwordEncoder.encode(request.getCurPassword()));
        // 사용자 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurPassword(), member.getPassword())) {
            throw new MemberException(INCORRECT_PASSWORD);
        }
        // 변경 후 비밀번호가 변경 전 비밀번호와 같을때
        if (passwordEncoder.encode(request.getPassword()).equals(member.getPassword())) {
            throw new MemberException(MATCH_PREVIOUS_PASSWORD);
        }

        Member updatedMember = Member.builder()
                .id(member.getId())
                .email(request.getEmail() != null ? request.getEmail() : member.getEmail())
                .name(request.getName() != null ? request.getName() : member.getName())
                .password(request.getPassword() != null ?
                        passwordEncoder.encode(request.getPassword()) : member.getPassword())
                .imageUrl(request.getImageUrl() != null ? request.getImageUrl()
                        : member.getImageUrl())
                .socialLoginType(request.getSocialType() != null ? request.getSocialType()
                        : member.getSocialLoginType())
                .account(request.getAccount() != null ? request.getAccount() : member.getAccount())
                .bankName(request.getBankName() != null ? request.getBankName()
                        : member.getBankName())
                .status(member.getStatus())
                .build();

        Member save = memberRepository.save(updatedMember);

        Response response = modelMapper.map(updatedMember, Response.class);

        return response;
    }

    public void deleteUser(long userId, ModifyRequest request) {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new MemberException(NOT_FOUND_USER));
        // 사용자 비밀번호 확인
        if (!passwordEncoder.matches(request.getCurPassword(), member.getPassword())) {
            throw new MemberException(INCORRECT_PASSWORD);
        }

        /**
         * 회원탈퇴 시 실제 DB 에서 Delete 를 하지 않고 유저 status 값을 탈퇴로 바꾼다
         * 탈퇴 처리한 유저가 다시 가입할 경우를 대비하여 이메일을 공백 값으로 둔다.
         */

        Member updateStatus = Member.builder()
                .id(member.getId())
                .email(" ")
                .name(member.getName())
                .password(" ")
                .imageUrl(member.getImageUrl())
                .socialLoginType(member.getSocialLoginType())
                .status(MemberStatus.QUIT.name())
                .build();

        memberRepository.save(updateStatus);
    }
}

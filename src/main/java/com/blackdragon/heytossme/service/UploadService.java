package com.blackdragon.heytossme.service;

import com.blackdragon.heytossme.component.LocalUploader;
import com.blackdragon.heytossme.component.S3Uploader;
import com.blackdragon.heytossme.dto.UploadFileDTO.Request;
import com.blackdragon.heytossme.exception.MemberException;
import com.blackdragon.heytossme.exception.errorcode.MemberErrorCode;
import com.blackdragon.heytossme.persist.MemberRepository;
import com.blackdragon.heytossme.persist.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UploadService {

    private final S3Uploader s3Uploader;

    private final LocalUploader localUploader;

    private final MemberRepository memberRepository;

    public String uploadImage(Long id, Request request) {

        String uploadedFilePaths = localUploader.uploadLocal(request.getFile());

        String url = s3Uploader.upload(uploadedFilePaths);

        Member member = memberRepository.findById(id).orElseThrow(() ->
                new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));

        member.setImageUrl(url);

        memberRepository.save(member);

        return url;
    }
}

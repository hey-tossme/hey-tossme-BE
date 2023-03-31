package com.blackdragon.heytossme.controller;

import com.blackdragon.heytossme.dto.ResponseForm;
import com.blackdragon.heytossme.dto.UploadFileDTO.Request;
import com.blackdragon.heytossme.service.UploadService;
import com.blackdragon.heytossme.type.UploadResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
@Log4j2
public class UploadController {

    private final UploadService uploadService;
    private static final String USER_ID = "userId";

    @PostMapping
    public ResponseEntity<ResponseForm> uploadImage(HttpServletRequest httpServletRequest,
            Request request) {
        log.info("uploadImage start");

        Long id = (Long) httpServletRequest.getAttribute(USER_ID);

        String url = uploadService.uploadImage(id, request);

        return ResponseEntity.ok(
                new ResponseForm(UploadResponse.SENT_URL.getMessage(), url)
        );
    }

}

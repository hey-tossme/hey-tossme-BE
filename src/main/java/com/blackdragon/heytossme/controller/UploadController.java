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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@Log4j2
public class UploadController {

    private final UploadService uploadService;

    @PostMapping
    public ResponseEntity<?> uploadImage(HttpServletRequest httpServletRequest,
            @RequestBody Request request) {

        long id = (long) httpServletRequest.getAttribute("id");

        String url = uploadService.uploadImage(id, request);

        return ResponseEntity.ok(
                new ResponseForm(UploadResponse.SENT_URL.getMessage(), url)
        );
    }

}

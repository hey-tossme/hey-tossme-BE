package com.blackdragon.heytossme.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileDTO {

    @Data
    public static class Request {

        private MultipartFile file;
    }

    @Data
    public static class Response {

        private String url;
    }
}

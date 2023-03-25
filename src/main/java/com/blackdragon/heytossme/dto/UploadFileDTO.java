package com.blackdragon.heytossme.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

public class UploadFileDTO {

    @Data
    public static class Request {

        private MultipartFile files;
    }

    @Data
    public static class Response {

        private String url;
    }
}

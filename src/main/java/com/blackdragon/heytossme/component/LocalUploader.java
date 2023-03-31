package com.blackdragon.heytossme.component;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Log4j2
public class LocalUploader {

    @Value("${com.blackdragon.upload.path}")
    private String uploadPath;

    public String uploadLocal(MultipartFile multipartFile) {

        if (multipartFile == null || multipartFile.isEmpty()) {
            return null;
        }

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + "_" + multipartFile.getOriginalFilename();

        Path savePath = Paths.get(uploadPath, saveFileName);

        try {
            multipartFile.transferTo(savePath);


        } catch (IOException e) {
            log.error("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        return savePath.toString();
    }
}

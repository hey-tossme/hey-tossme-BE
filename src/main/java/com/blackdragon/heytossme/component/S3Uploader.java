package com.blackdragon.heytossme.component;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.File;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;
    public String upload(String filePath) {

        File file = new File(filePath);

        String uploadImageUrl = putS3(file, file.getName());

        removeServerFile(file);

        log.info(uploadImageUrl);

        return uploadImageUrl;
    }

    private String putS3(File file, String fileName) {

        amazonS3.putObject(new PutObjectRequest(bucket, fileName, file).withCannedAcl(
                CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    private void removeServerFile(File file) {
        if (file.exists() && file.delete()) {
            log.info("파일이 성공적으로 삭제 되었습니다");
            return;
        }
        log.info("파일 삭제가 실패 했습니다.");
    }

    public void removeS3File(String fileName) {
        final DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(bucket, fileName);
        amazonS3.deleteObject(deleteObjectRequest);
    }
}

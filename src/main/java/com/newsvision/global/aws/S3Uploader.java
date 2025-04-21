package com.newsvision.global.aws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3Uploader {
    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    private final S3Client s3Client;

    public String upload(MultipartFile file, String keyName) {
        if (bucketName == null || region == null) {
            log.error("S3 버킷 설정이 누락되었습니다.");
            throw new IllegalStateException("S3 버킷 설정이 누락되었습니다.");
        }

        try {
            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(file.getContentType())
//                    .acl(ObjectCannedACL.PUBLIC_READ)
                    .build();

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
        } catch (Exception e) {
            log.error("S3 업로드 중 오류 발생: " + file.getOriginalFilename());
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }
}

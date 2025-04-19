package com.newsvision.global.aws;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
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
        try {
            log.warn("== MultipartFile 상태 확인 ==");
            log.warn("isEmpty: {}", file.isEmpty());
            log.warn("original filename: {}", file.getOriginalFilename());
            log.warn("content type: {}", file.getContentType());
            log.warn("file size: {}", file.getSize());

            log.warn("s3 업로더 file name1: {}", file.getOriginalFilename());
            log.warn("s3 업로더 content type1: {}", file.getContentType());
            log.warn("s3 업로더 file size1: {}", file.getSize());

            PutObjectRequest putRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(file.getContentType())
                    .build();

            log.warn("put", putRequest);
            log.warn("bucketName:{}", bucketName);
            log.warn("keyName:{}", keyName);
            log.warn("contentType:{}", file.getContentType());

            s3Client.putObject(putRequest, RequestBody.fromBytes(file.getBytes()));

            log.warn("s3 업로더 file name2: {}", file.getOriginalFilename());
            log.warn("s3 업로더 content type2: {}", file.getContentType());
            log.warn("s3 업로더 file size2: {}", file.getSize());
            return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + keyName;
        } catch (Exception e) {
            log.error("upload error", e);
            e.printStackTrace();
            log.error("upload error-=----------------");
            throw new RuntimeException("S3 업로드 실패", e);
        }
    }
}

package com.newsvision.global.aws;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploaderService {
    private final S3Uploader s3Uploader;

    public String uploadUserProfile(byte[] imageBytes, Long userId) {
        String keyName = "profiles/" + userId + "/" + UUID.randomUUID();
        return s3Uploader.upload(imageBytes, keyName);
    }

    public String uploadNewsImage(byte[] imageBytes, Long newsId) {
        String keyName = "news/" + newsId + "/" + UUID.randomUUID();
        return s3Uploader.upload(imageBytes, keyName);
    }

    public String uploadBoardImage(byte[] imageBytes, Long userId) {
        String keyName = "board/" + userId + "/" + UUID.randomUUID();
        return s3Uploader.upload(imageBytes, keyName);
    }
}

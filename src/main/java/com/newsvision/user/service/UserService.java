package com.newsvision.user.service;

import com.newsvision.global.aws.S3Uploader;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.response.UpdateUserResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final S3Uploader s3Uploader;

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public boolean existsByUsername(String username) {
       return userRepository.existsByUsername(username);
    }

    public boolean existsByNickname(String nickname) {
         return userRepository.existsByNickname(nickname);
    }

    @Transactional
    public void save(JoinUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .image("/images/default-profile.png")
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public UpdateUserResponse userInfo(Long id) {
        User user = findByUserId(id);
        log.warn(user.toString());
        return new UpdateUserResponse(user.getImage(), user.getNickname(), user.getIntroduce());
    }

    @Transactional
    public void updateUserProfile(Long id, MultipartFile image, String nickname, String introduce) {
        User user = findByUserId(id);
        if(image!=null && !image.isEmpty()){
            String keyNmae = "profile/" + user.getId() + "_" + UUID.randomUUID();
            String imageUrl = s3Uploader.upload(image, keyNmae);
            user.updateImage(imageUrl);
        }
        if (nickname != null && !nickname.equals(user.getNickname())) {
            user.updateNickname(nickname);
        }
        if (introduce != null && !introduce.equals(user.getIntroduce())) {
            user.updateIntroduce(introduce);
        }
    }
}

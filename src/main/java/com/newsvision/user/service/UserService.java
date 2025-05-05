package com.newsvision.user.service;

import com.newsvision.global.auth.AuthService;
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.global.jwt.JwtTokenProvider;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdatePasswordRequest;
import com.newsvision.user.dto.response.*;
import com.newsvision.user.entity.Badge;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final BadgeService badgeService;
    @Value("${custom.default-image-url}")
    private String defaultProfileImage;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileUploaderService fileUploaderService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;

    public User findByUserId(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPassword(String password, String checkPassword) {
        return password.equals(checkPassword);
    }

    public void validateRole(String role) {
        if (!("ROLE_ADMIN".equals(role) || "ROLE_CREATOR".equals(role))) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
    }

    @Transactional
    public void save(JoinUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        String nickname = "Newsion_User_" + UUID.randomUUID().toString().substring(0, 6);
            User user = User.builder()
                    .username(request.getUsername())
                    .password(encodedPassword)
                    .email(request.getEmail())
                    .image(defaultProfileImage)
                    .nickname(nickname)
                    .build();
            userRepository.save(user);

    }

    @Transactional
    public void delete(Long userId) {
        deleteProfileImage(userId, findByUserId(userId).getImage());
        userRepository.deleteById(userId);
    }

    public UpdateUserResponse userInfo(Long id) {
        User user = findByUserId(id);
        return UpdateUserResponse.from(user);
    }

    @Transactional
    public void updateUserProfile(Long id, MultipartFile image, String nickname, String email, String introduce) {
        User user = findByUserId(id);
        if (image != null && !image.isEmpty()) {
            String oldImageUrl = user.getImage();
            try {
                byte[] resizedImage = resizeProfileImage(image);
                String imageUrl = fileUploaderService.uploadUserProfile(resizedImage, user.getId());
                user.updateImage(imageUrl);

                if (oldImageUrl != null && !oldImageUrl.isEmpty() && !oldImageUrl.contains("default-profile.png")) {
                    fileUploaderService.deleteFile(oldImageUrl);
                }

            } catch (IOException e) {
                throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
            }
        }

        if (nickname != null && !nickname.equals(user.getNickname())) {
            user.updateNickname(nickname);
        }
        if (email != null && !email.equals(user.getEmail())) {
            user.updateEmail(email);
        }
        if (introduce != null && !introduce.equals(user.getIntroduce()) && !introduce.equals("null")) {
            user.updateIntroduce(introduce);
        }
    }

    @Transactional
    public void updateIsPaidOrUser(User user) {
        user.updateIsPaid(true);
        userRepository.save(user);
    }

    private byte[] resizeProfileImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(64, 64)
                .outputFormat("jpg")
                .outputQuality(0.9)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    @Transactional
    public void updatePassword(String tempToken, UpdatePasswordRequest request) {
        String username = jwtTokenProvider.getUsername(tempToken);
        User user = findByUsername(username);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.updatePassword(encodedPassword);
        authService.deleteTempToken(tempToken);
    }

    @Transactional
    public void deleteProfileImage(Long id, String imageUrl) {
        User user = findByUserId(id);
        if (!user.getImage().equals("default-profile.png")) {
            fileUploaderService.deleteFile(user.getImage());
        }
    }

    public void matchUserId(Long userId, Long id) {
        if(!userId.equals(id)){
            throw new CustomException(ErrorCode.FORBIDDEN_USER);
        }
    }

    public void checkAdminByUserId(String role) {
        if(!role.equals("ROLE_ADMIN")){
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }

    @Transactional
    public void updateUserRole(Long id) {
        User user = findByUserId(id);
        if (user.getRole().name().equals("ROLE_USER") || user.getRole().name().equals("ROLE_CREATOR")) {
            Badge creatorBadge = badgeService.findByBadgeId(2L);
            user.updateRole(creatorBadge);
        } else {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }
    }
}

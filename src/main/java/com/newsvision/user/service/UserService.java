package com.newsvision.user.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.user.dto.request.JoinUserRequest;
import com.newsvision.user.dto.request.UpdateUserRequest;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

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

//    public boolean checkRoleUser(String role){
//        return userRepository.findByRole(role);
//    }

    @Transactional
    public void save(JoinUserRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        User user = User.builder()
                .username(request.getUsername())
                .password(encodedPassword)
                .email(request.getEmail())
                .nickname(request.getNickname())
                .build();
        userRepository.save(user);
    }

    @Transactional
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    @Transactional
    public void update(UpdateUserRequest request) {
        User user = findByUserId(request.getId());
        user.update(request);
    }
}

package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class UserListService {
    private final UserRepository userRepository;


    @Autowired
    public UserListService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public List<UserListResponse> getMaxAllUsers() {
        List<User> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return users.stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
    }


    public List<UserListResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
    }

    public UserListResponse saveUser(UserListResponse dto) {
        try {
            User.Role role = User.Role.valueOf(dto.getRole());

            User user = User.builder()
                    .username(dto.getUsername())
                    .nickname(dto.getNickname())
                    .password(dto.getPassword())
                    .role(role)
                    .isPaid(dto.getIsPaid())
                    .createAt(dto.getCreate_at())
                    .introduce(dto.getIntroduce())
                    .image(dto.getImage())
                    .providerId(dto.getProvider_id())
                    .provider(dto.getProvider())
                    .build();

            return new UserListResponse(userRepository.save(user));

        } catch (Exception e) {
            e.printStackTrace(); // 서버 로그에 예외 표시
            throw new RuntimeException("사용자 저장 중 오류 발생: " + e.getMessage());
        }
    }



}

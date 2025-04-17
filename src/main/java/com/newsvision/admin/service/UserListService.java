package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserRepository userRepository;
    private final UserRepository userListRepository;

    public List<UserListResponse> getAllUsers() {
        return userListRepository.findAll().stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
    }

    public UserListResponse saveUser(UserListResponse userListResponse) {
        User userListEntity = User.builder()
                .username(userListResponse.getUsername())
                .nickname(userListResponse.getNickname())
                .role(userListResponse.getRole() != null ? User.Role.valueOf(userListResponse.getRole()) : User.Role.USER)
                .isPaid(userListResponse.getIsPaid() != null ? userListResponse.getIsPaid() : false)
                .createAt(userListResponse.getCreated_at())
                .build();
        User savedEntity = userListRepository.save(userListEntity);
        return new UserListResponse(savedEntity);
    }
}

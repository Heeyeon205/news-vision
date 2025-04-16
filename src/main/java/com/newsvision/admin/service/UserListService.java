package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.UserListResponse;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class UserListService {
    private final UserRepository userRepository;


    public List<UserListResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserListResponse::new)
                .collect(Collectors.toList());
    }
}

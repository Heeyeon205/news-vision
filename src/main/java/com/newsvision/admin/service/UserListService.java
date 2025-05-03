package com.newsvision.admin.service;

import com.newsvision.admin.dto.response.UserListResponse;
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
}

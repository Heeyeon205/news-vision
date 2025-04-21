package com.newsvision.admin.service;

import com.newsvision.poll.controller.response.PollResponse;
import com.newsvision.poll.repository.PollRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class PollListServicer {
    private final PollRepository pollRepository;


    public List<PollResponse> getAllPolls() {
        return pollRepository.findAll().stream()
                .map(poll -> PollResponse.builder()
                        .id(poll.getId())
                        .title(poll.getTitle())
                        .content(poll.getContent())
                        .createdAt(poll.getCreatedAt())
                        .expiredAt(poll.getExpiredAt())
                        .authorNickname(poll.getUser().getNickname())
                        .build())
                .collect(Collectors.toList());
    }
}

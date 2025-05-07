package com.newsvision.poll.service;

import com.newsvision.global.Utils.TimeUtil;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.poll.dto.request.CreatePollRequest;
import com.newsvision.poll.dto.request.UpdatePollRequest;
import com.newsvision.poll.dto.request.VoteRequest;
import com.newsvision.poll.dto.response.PollListResponse;
import com.newsvision.poll.dto.response.PollOptionResponse;
import com.newsvision.poll.dto.response.PollResponse;
import com.newsvision.poll.entity.Poll;
import com.newsvision.poll.entity.PollOption;
import com.newsvision.poll.entity.PollVote;
import com.newsvision.poll.repository.PollOptionRepository;
import com.newsvision.poll.repository.PollRepository;
import com.newsvision.poll.repository.PollVoteRepository;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollVoteService {
    private final PollVoteRepository pollVoteRepository;

    public boolean existsByPollVote(Long pollId, Long userId) {
        return pollVoteRepository.existsByPollOption_Poll_IdAndUser_Id(pollId, userId);
    }

    @Transactional
    public void deleteByUserId(Long id) {
        pollVoteRepository.deleteByUserId(id);
    }
}
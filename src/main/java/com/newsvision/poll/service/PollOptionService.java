package com.newsvision.poll.service;


import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.poll.entity.PollOption;
import com.newsvision.poll.repository.PollOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PollOptionService {
    private final PollOptionRepository pollOptionRepository;

    public PollOption findById(Long id) {
        return pollOptionRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }
}

package com.newsvision.poll.service;

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

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PollService {
    private final PollRepository pollRepository;
    private final UserService userService;
    private final PollOptionService pollOptionService;

    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;

    public Poll findById(Long id) {
        return pollRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public List<PollListResponse> getAllList() {
        LocalDateTime now = LocalDateTime.now();
        List<Poll> polls = pollRepository.findAllByExpiredAtAfter(now);
        return polls.stream()
                .map(PollListResponse::new)
                .toList();
    }

    @Transactional
    public PollResponse createPoll(CreatePollRequest request, Long userId) {
            User user = userService.findByUserId(userId);
            Poll poll = new Poll();
            poll.setTitle(request.getTitle());
            poll.setContent(request.getContent());
            poll.setExpiredAt(request.getExpiredAt());
            poll.setUser(user);
            pollRepository.save(poll);

            List<PollOption> pollOptions = request.getOptions().stream()
                    .map(optionContent->{
                        PollOption option = new PollOption();
                        option.setContent(optionContent);
                        option.setPoll(poll);
                        return option;
                    })
                    .collect(Collectors.toList());
            pollOptionRepository.saveAll(pollOptions);
            poll.setPollOptions(pollOptions);

            return convertToPollResponse(poll);
    }

    @Transactional
    public PollResponse updatePoll(Long pollId, UpdatePollRequest request, Long userId) {
        Poll poll = findById(pollId);
        userService.matchUserId(userId, poll.getUser().getId());

        // 만료일 검증
        LocalDateTime newExpiredAt = request.getExpiredAt();
        if (newExpiredAt == null) {
            newExpiredAt = poll.getExpiredAt(); // 기존 값 유지
        } else if (newExpiredAt.isBefore(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 선택지 검증 (컨트롤러에서 이미 검증됨, 추가 확인)
        if (request.getOptions() == null || request.getOptions().size() < 2) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }

        // 투표 정보 업데이트
        poll.setTitle(request.getTitle());
        poll.setContent(request.getContent());
        poll.setExpiredAt(newExpiredAt);

        // 기존 선택지 삭제 (DB에서 직접 삭제)
        pollOptionRepository.deleteByPollId(pollId);

        // poll 객체의 pollOptions 리스트 초기화 (삭제된 객체 참조 방지)
        poll.getPollOptions().clear();

        // 새 선택지 추가
        List<PollOption> newOptions = request.getOptions().stream()
                .map(optionContent -> {
                    PollOption option = new PollOption();
                    option.setContent(optionContent);
                    option.setPoll(poll);
                    option.setCount(0);
                    return option;
                })
                .collect(Collectors.toList());

        // 새로운 선택지를 pollOptions에 추가
        poll.getPollOptions().addAll(newOptions);

        // poll 저장 (cascade로 인해 pollOptions도 함께 저장됨)
        pollRepository.save(poll);
        return convertToPollResponse(poll);
    }

    public boolean checkVote(Long userId, Long pollId) {
        return pollVoteRepository.existsByUserIdAndPollOption_Poll_Id(userId, pollId);
    }

    @Transactional
    public void vote(VoteRequest request, Long userId) {
        User user = userService.findByUserId(userId);
        PollOption pollOption = pollOptionService.findById(request.getOptionId());
        Poll poll = pollOption.getPoll();

        if(checkVote(user.getId(), poll.getId())){
            throw new CustomException(ErrorCode.DUPLICATE_VOTE);
        }

        PollVote pollVote = new PollVote();
        pollVote.setUser(user);
        pollVote.setPollOption(pollOption);
        pollVoteRepository.save(pollVote);

        pollOption.setCount(pollOption.getCount() + 1);
        pollOptionRepository.save(pollOption);
    }

    public PollResponse getPoll(Long pollId) {
        Poll poll = findById(pollId);
        return convertToPollResponse(poll);
    }

    private PollResponse convertToPollResponse(Poll poll) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setTitle(poll.getTitle());
        response.setContent(poll.getContent());
        response.setCreatedAt(poll.getCreatedAt());
        response.setExpiredAt(poll.getExpiredAt());
        response.setNickname(poll.getUser() != null ? poll.getUser().getNickname() : "Unknown");
        response.setPollOptions(poll.getPollOptions().stream()
                .map(option -> {
                    PollOptionResponse optionResponse = new PollOptionResponse();
                    optionResponse.setId(option.getId());
                    optionResponse.setContent(option.getContent());
                    optionResponse.setCount(option.getCount());
                    return optionResponse;
                })
                .collect(Collectors.toList()));
        return response;
    }

    @Transactional
    public void deletePoll(Long pollId, Long userId) {
        Poll poll = findById(pollId);
        userService.matchUserId(userId, poll.getUser().getId());
        pollVoteRepository.deleteByPollOption_Poll_Id(pollId);
        pollRepository.delete(poll);
    }
}

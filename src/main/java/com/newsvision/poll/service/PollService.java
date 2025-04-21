package com.newsvision.poll.service;

import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.poll.controller.request.CreatePollRequest;
import com.newsvision.poll.controller.request.VoteRequest;
import com.newsvision.poll.controller.response.PollOptionResponse;
import com.newsvision.poll.controller.response.PollResponse;
import com.newsvision.poll.entity.Poll;
import com.newsvision.poll.entity.PollOption;
import com.newsvision.poll.entity.PollVote;
import com.newsvision.poll.repository.PollOptionRepository;
import com.newsvision.poll.repository.PollRepository;
import com.newsvision.poll.repository.PollVoteRepository;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PollService {
    private final PollRepository pollRepository;
    private final PollOptionRepository pollOptionRepository;
    private final PollVoteRepository pollVoteRepository;
    private final UserRepository userRepository;

    @Transactional
    public PollResponse createPoll(CreatePollRequest request, Long userId) {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

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
    public void vote(VoteRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        PollOption pollOption = pollOptionRepository.findById(request.getOptionId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        Poll poll = pollOption.getPoll();

        //중복투표 방지
        if(pollVoteRepository.existsByUserIdAndPollOption_Poll_Id(user.getId(), poll.getId())){
            throw new CustomException(ErrorCode.DUPLICATE_VOTE);

        }

        PollVote pollVote = new PollVote();
        pollVote.setUser(user);
        pollVote.setPollOption(pollOption);
        pollVoteRepository.save(pollVote);

        pollOption.setCount(pollOption.getCount() + 1);
        pollOptionRepository.save(pollOption);
    }
    @Transactional(readOnly = true)
    public PollResponse getPoll(Long pollId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND)); // Poll을 찾을 수 없을 때 발생
        return convertToPollResponse(poll);
    }

    private PollResponse convertToPollResponse(Poll poll) {
        PollResponse response = new PollResponse();
        response.setId(poll.getId());
        response.setTitle(poll.getTitle());
        response.setContent(poll.getContent());
        response.setCreatedAt(poll.getCreatedAt());
        response.setExpiredAt(poll.getExpiredAt());
        response.setAuthorNickname(poll.getUser().getNickname());
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


}

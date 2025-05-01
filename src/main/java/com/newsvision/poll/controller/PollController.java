package com.newsvision.poll.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.poll.dto.request.CreatePollRequest;
import com.newsvision.poll.dto.request.UpdatePollRequest;
import com.newsvision.poll.dto.request.VoteRequest;
import com.newsvision.poll.dto.response.PollResponse;
import com.newsvision.poll.dto.response.PollVoteResponse;
import com.newsvision.poll.repository.PollVoteRepository;
import com.newsvision.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polls")
public class PollController {
    private final PollService pollService;
    private final PollVoteRepository pollVoteRepository;

    @PostMapping()
    public ResponseEntity<ApiResponse<PollResponse>> createPoll(
            @RequestBody CreatePollRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        PollResponse response = pollService.createPoll(request,userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{pollId}")
    public ResponseEntity<ApiResponse<PollResponse>> updatePoll(
            @PathVariable Long pollId,
            @RequestBody UpdatePollRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ){
        Long userId = userDetails.getId();
        PollResponse response = pollService.updatePoll(pollId,request,userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<ApiResponse<PollVoteResponse>> vote(
            @PathVariable Long pollId,
            @RequestBody VoteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        pollService.vote(request, userId);
        int voteCount = pollVoteRepository.countByPollOption_Poll_Id(pollId);
        return ResponseEntity.ok(ApiResponse.success(new PollVoteResponse(voteCount, pollService.checkVote(pollId, userId))));
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<ApiResponse<PollResponse>> getPoll(@PathVariable Long pollId) {
        PollResponse response = pollService.getPoll(pollId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{pollId}")
    public ResponseEntity<ApiResponse<Void>> deletePoll(
            @PathVariable Long pollId
          , @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getId();
        pollService.deletePoll(pollId,userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}

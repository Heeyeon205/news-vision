package com.newsvision.poll.controller;

import com.newsvision.global.exception.ApiResponse;
import com.newsvision.global.security.CustomUserDetails;
import com.newsvision.poll.controller.request.CreatePollRequest;
import com.newsvision.poll.controller.request.VoteRequest;
import com.newsvision.poll.controller.response.PollResponse;
import com.newsvision.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polls")
public class PollController {
    private final PollService pollService;

    @PostMapping
    public ResponseEntity<ApiResponse<PollResponse>> createPoll(
            @RequestBody CreatePollRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        PollResponse response = pollService.createPoll(request,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<ApiResponse<Void>> vote(
            @PathVariable Long pollId,
            @RequestBody VoteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        pollService.vote(request, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<ApiResponse<PollResponse>> getPoll(@PathVariable Long pollId) {
        PollResponse response = pollService.getPoll(pollId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}

package com.newsvision.poll.controller;

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
    public ResponseEntity<PollResponse> createPoll(@RequestBody CreatePollRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        PollResponse response = pollService.createPoll(request, userDetails.getUsername());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/{pollId}/vote")
    public ResponseEntity<Void> vote(@PathVariable Long pollId, @RequestBody VoteRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        pollService.vote(request, userDetails.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{pollId}")
    public ResponseEntity<PollResponse> getPoll(@PathVariable Long pollId) {
        PollResponse response = pollService.getPoll(pollId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

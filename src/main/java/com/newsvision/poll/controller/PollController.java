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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/polls")
@Tag(name = "투표 API", description = "투표 생성, 조회, 투표 참여 등 투표 관련 기능 제공")
public class PollController {
    private final PollService pollService;
    private final PollVoteRepository pollVoteRepository;

    @Operation(summary = "투표 상세 조회", description = "투표 ID로 해당 투표의 상세 정보를 조회합니다.")
    @GetMapping("/{pollId}")
    public ResponseEntity<ApiResponse<PollResponse>> getPoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails != null ? userDetails.getId() : null;
        PollResponse response = pollService.getPoll(pollId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "투표 생성", description = "사용자가 새로운 투표를 생성합니다.")
    @PostMapping
    public ResponseEntity<ApiResponse<Long>> createPoll(
            @RequestBody CreatePollRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        PollResponse response = pollService.createPoll(request, userId);
        return ResponseEntity.ok(ApiResponse.success(response.getId()));
    }

    @Operation(summary = "투표 수정", description = "기존 투표 정보를 수정합니다.")
    @PutMapping("/{pollId}")
    public ResponseEntity<ApiResponse<PollResponse>> updatePoll(
            @PathVariable Long pollId,
            @RequestBody UpdatePollRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        PollResponse response = pollService.updatePoll(pollId, request, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @Operation(summary = "투표 참여", description = "사용자가 선택한 항목에 대해 투표를 진행합니다.")
    @PostMapping("/{pollId}/vote")
    public ResponseEntity<ApiResponse<PollVoteResponse>> vote(
            @PathVariable Long pollId,
            @RequestBody VoteRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        pollService.vote(request, userId);
        int voteCount = pollVoteRepository.countByPollOption_Poll_Id(pollId);
        boolean hasVoted = pollService.checkVote(pollId, userId);
        return ResponseEntity.ok(ApiResponse.success(new PollVoteResponse(voteCount, hasVoted)));
    }

    @Operation(summary = "투표 삭제", description = "사용자가 자신의 투표를 삭제합니다.")
    @DeleteMapping("/{pollId}")
    public ResponseEntity<ApiResponse<Void>> deletePoll(
            @PathVariable Long pollId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        Long userId = userDetails.getId();
        pollService.deletePoll(pollId, userId);
        return ResponseEntity.ok(ApiResponse.success());
    }
}

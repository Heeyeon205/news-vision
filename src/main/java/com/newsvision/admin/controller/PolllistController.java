package com.newsvision.admin.controller;

import com.newsvision.admin.service.PollListServicer;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.poll.controller.response.PollResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/polls")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PolllistController {
    private final PollListServicer pollService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PollResponse>>> getPollList() {
        return ResponseEntity.ok(ApiResponse.success(pollService.getAllPolls()));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<PollResponse>>> getMaxPollList() {
        return ResponseEntity.ok(ApiResponse.success(pollService.getMaxAllPolls()));
    }


    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
        return ResponseEntity.ok(ApiResponse.success());
    }
}

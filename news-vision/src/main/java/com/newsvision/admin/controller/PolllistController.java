package com.newsvision.admin.controller;

import com.newsvision.admin.service.PollListServicer;

import com.newsvision.global.exception.CustomException;
import com.newsvision.poll.controller.response.PollResponse;
import com.newsvision.poll.service.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<PollResponse>> getPollList() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }

    @GetMapping("/max")
    public ResponseEntity<List<PollResponse>> getMaxPollList() {
        return ResponseEntity.ok(pollService.getMaxAllPolls());
    }


    // 카테고리 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
        return ResponseEntity.noContent().build();
    }
}

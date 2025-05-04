package com.newsvision.admin.controller;

import com.newsvision.admin.service.PollListServicer;
import com.newsvision.global.exception.ApiResponse;
import com.newsvision.poll.dto.response.PollResponse;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Hidden
@RestController
@RequestMapping("/admin/polls")
@RequiredArgsConstructor
public class PollListController {
    private final PollListServicer pollService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<PollResponse>>> getPollList(   @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(ApiResponse.success(pollService.getAllPolls()));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<PollResponse>>> getMaxPollList(   @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(ApiResponse.success(pollService.getMaxAllPolls()));
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> deletePoll(@PathVariable Long id  ,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }
        pollService.deletePoll(id);
        return ResponseEntity.ok(ApiResponse.success("투표(ID: " + id + ") 삭제 완료"));
    }
}

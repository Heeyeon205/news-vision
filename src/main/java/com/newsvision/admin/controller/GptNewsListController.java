package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.GptNewsResponse;
import com.newsvision.admin.service.GptNewsListService;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@Hidden
@RestController
@RequestMapping("/admin/gptnews")
public class GptNewsListController {
    private final GptNewsListService gptNewsService;
    @Autowired
    public GptNewsListController(GptNewsListService gptNewsService) {
        this.gptNewsService = gptNewsService;
    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<GptNewsResponse>>> getAllGptNews(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<GptNewsResponse> gptNewsResponses = gptNewsService.getAllGptNews();
        return ResponseEntity.ok(ApiResponse.success(gptNewsResponses));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<GptNewsResponse>>> getMaxAllGptNews(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<GptNewsResponse> gptNewsResponses = gptNewsService.getMaxAllGptNews();
        return ResponseEntity.ok(ApiResponse.success(gptNewsResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteGptNews(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        gptNewsService.deleteGptNews(id);
        return ResponseEntity.ok(ApiResponse.success("GPT 뉴스(ID: " + id + ") 삭제 완료"));
    }

}

package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.NaverNewsResponse;
import com.newsvision.admin.service.NaverNewsListService;
import com.newsvision.global.exception.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Hidden
@RequestMapping("/admin/navernews")
public class NaverNewsListController {

    private final NaverNewsListService naverNewsService;

    public NaverNewsListController(NaverNewsListService naverNewsService) {
        this.naverNewsService = naverNewsService;
    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<NaverNewsResponse>>> getAllNaverNews(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getAllNaverNews();
        return ResponseEntity.ok(ApiResponse.success(naverNewsResponses));
    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<NaverNewsResponse>>> getMaxAllNaverNews(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getMaxAllNaverNews();
        return ResponseEntity.ok(ApiResponse.success(naverNewsResponses));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteNaverNews(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null || !userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(403).body(null);
        }

        naverNewsService.deleteNaverNews(id);
        return ResponseEntity.ok(ApiResponse.success("네이버 뉴스(ID: " + id + ") 삭제 완료"));
    }
}

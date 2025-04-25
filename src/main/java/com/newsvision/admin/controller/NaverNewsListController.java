package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.NaverNewsResponse;
import com.newsvision.admin.service.NaverNewsListService;
import com.newsvision.global.exception.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/navernews")
public class NaverNewsListController {

    private final NaverNewsListService naverNewsService;

    public NaverNewsListController(NaverNewsListService naverNewsService) {
        this.naverNewsService = naverNewsService;
    }



    @GetMapping
    public ResponseEntity<ApiResponse<List<NaverNewsResponse>>> getAllNaverNews() {
        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getAllNaverNews();
        return ResponseEntity.ok(ApiResponse.success(naverNewsResponses));

    }

    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<NaverNewsResponse>>> getMaxAllNaverNews() {
        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getMaxAllNaverNews();
        return ResponseEntity.ok(ApiResponse.success(naverNewsResponses));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNaverNews(@PathVariable Long id) {
        naverNewsService.deleteNaverNews(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

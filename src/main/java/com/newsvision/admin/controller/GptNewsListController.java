package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.GptNewsResponse;
import com.newsvision.admin.service.GptNewsListService;
import com.newsvision.global.exception.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/gptnews")
public class GptNewsListController {
    private final GptNewsListService gptNewsService;
    @Autowired
    public GptNewsListController(GptNewsListService gptNewsService) {
        this.gptNewsService = gptNewsService;
    }


    @GetMapping
    public ResponseEntity<ApiResponse<List<GptNewsResponse>>> getAllGptNews() {
        List<GptNewsResponse> gptNewsResponses = gptNewsService.getAllGptNews();
        return ResponseEntity.ok(ApiResponse.success(gptNewsResponses));
    }
    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<GptNewsResponse>>> getMaxAllGptNews() {
        List<GptNewsResponse> gptNewsResponses = gptNewsService.getMaxAllGptNews();
        return ResponseEntity.ok(ApiResponse.success(gptNewsResponses));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGptNews(@PathVariable Long id) {
        gptNewsService.deleteGptNews(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

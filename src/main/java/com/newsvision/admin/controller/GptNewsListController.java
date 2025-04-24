package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.GptNewsResponse;
import com.newsvision.admin.service.GptNewsListService;
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
    public ResponseEntity<List<GptNewsResponse>> getAllGptNews() {
        List<GptNewsResponse> gptNewsResponses = gptNewsService.getAllGptNews();
        return ResponseEntity.ok(gptNewsResponses);
    }
    @GetMapping("/max")
    public ResponseEntity<List<GptNewsResponse>> getMaxAllGptNews() {
        List<GptNewsResponse> gptNewsResponses = gptNewsService.getMaxAllGptNews();
        return ResponseEntity.ok(gptNewsResponses);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGptNews(@PathVariable Long id) {
        gptNewsService.deleteGptNews(id);
        return ResponseEntity.noContent().build();
    }
}

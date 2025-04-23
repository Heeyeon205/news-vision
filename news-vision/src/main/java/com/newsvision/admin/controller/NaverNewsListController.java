package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.NaverNewsResponse;
import com.newsvision.admin.service.NaverNewsListService;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<NaverNewsResponse>> getAllNaverNews() {
        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getAllNaverNews();
        return ResponseEntity.ok(naverNewsResponses);
    }

    @GetMapping("/max")
    public ResponseEntity<List<NaverNewsResponse>> getMaxAllNaverNews() {
        List<NaverNewsResponse> naverNewsResponses = naverNewsService.getMaxAllNaverNews();
        return ResponseEntity.ok(naverNewsResponses);
    }

   @DeleteMapping("/{id}")
   public ResponseEntity<Void> deleteNaverNews(@PathVariable Long id) {
       naverNewsService.deleteNaverNews(id);
       return ResponseEntity.noContent().build();
   }
}

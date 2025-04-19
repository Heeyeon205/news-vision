package com.newsvision.admin.controller;
import com.newsvision.admin.service.NewsListService;
import com.newsvision.global.exception.CustomException; // Assuming you have this
import com.newsvision.global.exception.ErrorCode;     // Assuming you have this
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse; // Assuming you have this response class
import com.newsvision.news.service.NewsService;
import com.newsvision.user.entity.User; // Assuming this is your User entity

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // Import for getting logged-in user
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/admin/news")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class NewsListController {

    private final NewsListService newsService;


    @GetMapping
    public ResponseEntity<List<NewsResponse>> getNewsList() {
        List<NewsResponse> newsList = newsService.getAllNews();
        return ResponseEntity.ok(newsList);
    }

//
//   @GetMapping("/top10-admin")
//   public ResponseEntity<List<NewsSummaryResponse>> getTop10AdminNews() {
//       List<NewsSummaryResponse> topNews = newsService.getTop10RecentNewsOnlyByAdmin();
//       return ResponseEntity.ok(topNews);
//   }
//
//
//   @GetMapping("/creator")
//   public ResponseEntity<List<NewsSummaryResponse>> getCreatorNews() {
//       List<NewsSummaryResponse> creatorNews = newsService.getCreatorNewsList();
//       return ResponseEntity.ok(creatorNews);
//   }
//
//
//  @GetMapping("/scraps/me")
//  public ResponseEntity<List<NewsSummaryResponse>> getMyScraps(
//          @AuthenticationPrincipal User loginUser) {
//
//      if (loginUser == null) {
//
//           throw new CustomException(ErrorCode.UNAUTHORIZED);
//      }
//       List<NewsSummaryResponse> scrapList = newsService.getMyScrapList(loginUser);
//      return ResponseEntity.ok(scrapList);
//    }


}
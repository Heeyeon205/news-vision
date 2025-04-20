package com.newsvision.news.service;

import com.newsvision.category.entity.Categories;
import com.newsvision.category.repository.CategoryRepository;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.request.NewsCreateRequest;
import com.newsvision.news.controller.request.NewsUpdateRequest;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.news.repository.ScrapRepository;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {
    private final NewsRepository newsRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final ScrapRepository scrapRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    //
    public List<NewsSummaryResponse> getTop10RecentNewsOnlyByAdmin() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> topNews = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));
        return topNews.stream()
                .map(NewsSummaryResponse::from)
                .toList();
    }

    @Transactional
    public NewsResponse getNewsDetail(Long newsId, User loginUser) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        news.setView(news.getView() + 1); // 조회수 증가

        int likeCount = newsLikeRepository.countByNews(news);
        boolean liked = loginUser != null && newsLikeRepository.existsByUserAndNews(loginUser, news);
        boolean scraped = false;//loginUser != null && ScrapRepository.existsByUserAndNews(loginUser, news);
        return NewsResponse.of(news, likeCount, liked, scraped);
    }


    @Transactional
    public void addLike(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean exists = newsLikeRepository.existsByUserAndNews(user, news);
        if (!exists) {
            NewsLike like = NewsLike.builder()
                    .news(news)
                    .user(user)
                    .build();
            newsLikeRepository.save(like);
        }
    }

    @Transactional
    public void removeLike(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        newsLikeRepository.deleteByUserAndNews(user, news);
    }

    @Transactional
    public void addScrap(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        boolean exists = scrapRepository.existsByUserAndNews(user, news);
        if (!exists) {
            Scrap scrap = Scrap.builder()
                    .news(news)
                    .user(user)
                    .build();
            scrapRepository.save(scrap);
        }
    }

    @Transactional
    public void removeScrap(Long newsId, User user) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        scrapRepository.deleteByUserAndNews(user, news);
    }


    public List<NewsSummaryResponse> getMyScrapList(User user) {
        return scrapRepository.findAllByUser(user).stream()
                .map(scrap -> NewsSummaryResponse.from(scrap.getNews()))
                .toList();
    }

    public List<NewsSummaryResponse> getCreatorNewsList() {
        return newsRepository.findAllByCreatorOrderByCreatedAtDesc()
                .stream()
                .map(NewsSummaryResponse::from)
                .toList();
    }

    public Page<NewsSummaryResponse> getNewsListByCreatedAt(Pageable pageable) {
        return newsRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(NewsSummaryResponse::from);
    }

    public Page<NewsSummaryResponse> getFilteredArticles(String type, Long categoryId, User user, Pageable pageable) {
        Page<News> result;

        switch (type) {
            case "popular":
                result = newsRepository.findAllOrderByLikeCountDesc(pageable);
                break;
            case "follow":
                if (user == null) throw new CustomException(ErrorCode.UNAUTHORIZED);
                result = newsRepository.findByFollowingUsers(user.getId(), pageable);
                break;
            case "category":
                result = newsRepository.findByCategoryId(categoryId, pageable);
                break;
            case "recent":
            default:
                result = newsRepository.findAllByOrderByCreatedAtDesc(pageable);
                break;
        }


        return result.map(NewsSummaryResponse::from);
    }

    @Transactional
    public void createNews(Long userId, NewsCreateRequest request) {
        log.info("📌 뉴스 작성 요청 - userId: {}, categoryId: {}", userId, request.getCategoryId());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("👤 작성자 정보 - username: {}, role: {}", user.getUsername(), user.getRole());

        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        log.info("📂 카테고리 정보 - id: {}, name: {}", category.getId(), category.getName());

        News news = News.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .image(request.getImage())
                .user(user)
                .category(category)
                .createdAt(LocalDateTime.now())  // ✅ 꼭 넣어야 함!!
                .build();

        log.info("📰 News 객체 생성 완료 - title: {}, user: {}, category: {}", news.getTitle(), news.getUser().getUsername(), news.getCategory().getName());

        News saved = newsRepository.save(news);
    }

    @Transactional
    public void updateNews(Long userId, NewsUpdateRequest request) {
        News news = newsRepository.findById(request.getNewsId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!news.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Categories category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        log.info("✏️ 뉴스 수정 요청 - newsId: {}, by userId: {}", request.getNewsId(), userId);

        news.setTitle(request.getTitle());
        news.setContent(request.getContent());
        news.setImage(request.getImage());
        news.setCategory(category);
    }

    @Transactional
    public void deleteNews(Long userId, Long newsId) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!news.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        log.info("🗑️ 뉴스 삭제 요청 - newsId: {}, by userId: {}", newsId, userId);

        // 💡 좋아요 먼저 삭제
        newsLikeRepository.deleteAllByNews(news);
        log.info("👍 관련된 좋아요 삭제 완료");

        // 💡 스크랩도 삭제
        scrapRepository.deleteAllByNews(news);
        log.info("📌 관련된 스크랩 삭제 완료");

        // 💡 뉴스 삭제
        newsRepository.delete(news);
        log.info("📰 뉴스 삭제 완료");
    }



}

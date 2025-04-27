package com.newsvision.news.service;

import com.newsvision.category.Categories;
import com.newsvision.category.CategoryRepository;
import com.newsvision.category.CategoryService;
import com.newsvision.elasticsearch.service.NewsSearchService;
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.controller.response.NewsResponse;
import com.newsvision.news.controller.response.NewsSummaryResponse;
import com.newsvision.news.dto.response.NewsDetailInfoResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import com.newsvision.news.entity.NewsLike;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.repository.NaverNewsRepository;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.news.repository.ScrapRepository;
import com.newsvision.user.entity.User;
import com.newsvision.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsService {

    private final CategoryService categoryService;
    @Value("${custom.default-image-news-culture-url}")
    private String defaultCultureImage;
    @Value("${custom.default-image-news-book-url}")
    private String defaultBookImage;
    @Value("${custom.default-image-news-history-url}")
    private String defaultHistoryImage;
    @Value("${custom.default-image-news-art-url}")
    private String defaultArtImage;
    @Value("${custom.default-image-news-economy-url}")
    private String defaultEconomyImage;
    @Value("${custom.default-image-news-science-url}")
    private String defaultScienceImage;
    @Value("${custom.default-image-news-politics-url}")
    private String defaultPoliticsImage;
    @Value("${custom.default-image-news-global-url}")
    private String defaultGlobalImage;

    private final NewsRepository newsRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final ScrapRepository scrapRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final NaverNewsRepository naverNewsRepository;
    private final NewsSearchService newsSearchService;
    private final FileUploaderService fileUploaderService;

    public News findByNewsId(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public List<NewsSummaryResponse> getTop10RecentNewsOnlyByAdmin() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> topNews = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));
        return topNews.stream()
                .map(NewsSummaryResponse::from)
                .toList();
    }

    @Transactional
    public NewsResponse getNewsDetail(Long newsId, User loginUser) {
        News news = findByNewsId(newsId);
        news.increaseView();
        int likeCount = newsLikeRepository.countByNews(news);
        boolean liked = loginUser != null && newsLikeRepository.existsByUserAndNews(loginUser, news);
        boolean scraped = false;
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
        try {
            log.info("🧹 removeLike 호출 - newsId: {}, userId: {}", newsId, user.getId());
            News news = newsRepository.findById(newsId)
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
            newsLikeRepository.deleteByUserAndNews(user, news);
            log.info("✅ 삭제 성공");
        } catch (Exception e) {
            log.error("🔥 좋아요 삭제 중 오류 발생", e);
            throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
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
    public void createNews(Long userId, String title, String content, Long categoryId, Long naverNewsId, MultipartFile image) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
        NaverNews naverNews = naverNewsRepository.findById(naverNewsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                byte[] resizedImage = resizeNewsImage(image);
                imageUrl = fileUploaderService.uploadNewsImage(resizedImage, userId);
            } else {
                imageUrl = getDefaultImageForCategoryId(category.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패", e);
        }

        News news = News.builder()
                .title(title)
                .content(content)
                .user(user)
                .category(category)
                .naverNews(naverNews)
                .image(imageUrl)
                .build();

        News saved = newsRepository.save(news);

        try {
            newsSearchService.saveNews(saved);
        } catch (Exception e) {
            log.error("❌ Elasticsearch 저장 실패", e);
        }
    }

    @Transactional
    public void updateNews(Long newsId, Long userId, String title, String content, Long categoryId, MultipartFile image) {
        News news = newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        if (!news.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }

        Categories category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

        String imageUrl = news.getImage();
        if (image != null && !image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                imageUrl = fileUploaderService.uploadNewsImage(imageBytes, userId);
            } catch (Exception e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }

        news.setTitle(title);
        news.setContent(content);
        news.setImage(imageUrl);
        news.setCategory(category);

        newsSearchService.saveNews(news);
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

        // ✅ Elasticsearch에서도 제거
        newsSearchService.deleteNews(newsId);
    }

    private byte[] resizeNewsImage(MultipartFile file) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(file.getInputStream())
                .size(530, 300)
                .outputFormat("jpg")
                .outputQuality(0.9)
                .toOutputStream(outputStream);
        return outputStream.toByteArray();
    }

    private String getDefaultImageForCategoryId(Long categoryId) {
        return switch (categoryId.intValue()) {
            case 2 -> defaultEconomyImage; // 경제
            case 3 -> defaultPoliticsImage; // 정치, 사회
            case 4 -> defaultCultureImage; // 문화
            case 5 -> defaultGlobalImage; // 글로벌
            case 6 -> defaultArtImage; // 예술
            case 7 -> defaultScienceImage; // 과학기술
            case 8 -> defaultHistoryImage; // 역사
            case 9 -> defaultBookImage; // 도서, 문학
            default -> defaultGlobalImage; // 미분류 또는 기타
        };
    }

    public int countByNews(News news) {
        return newsLikeRepository.countByNews(news);
    }

    public NewsDetailInfoResponse newsInfo(Long newsId) {
        News news = findByNewsId(newsId);
        List<Categories> categories = categoryService.findAllList();
        return NewsDetailInfoResponse.from(news, categories);
    }
}

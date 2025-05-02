package com.newsvision.news.service;


import com.newsvision.category.Categories;
import com.newsvision.category.CategoryRepository;
import com.newsvision.category.CategoryResponse;
import com.newsvision.category.CategoryService;
import com.newsvision.elasticsearch.service.NewsSearchService;
import com.newsvision.global.aws.FileUploaderService;
import com.newsvision.global.exception.CustomException;
import com.newsvision.global.exception.ErrorCode;
import com.newsvision.news.dto.response.*;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.entity.News;
import com.newsvision.news.repository.NaverNewsRepository;
import com.newsvision.news.repository.NewsLikeRepository;
import com.newsvision.news.repository.NewsRepository;
import com.newsvision.news.repository.ScrapRepository;
import com.newsvision.poll.dto.response.PollListResponse;
import com.newsvision.poll.service.PollService;
import com.newsvision.user.entity.User;
import com.newsvision.user.service.FollowService;
import com.newsvision.user.service.UserService;
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
@Transactional(readOnly = true)
public class NewsService {
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
    private final NewsSearchService newsSearchService;
    private final FileUploaderService fileUploaderService;
    private final NewsLikeService newsLikeService;
    private final ScrapService scrapService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final PollService pollService;
    private final FollowService followService;

    private final CategoryRepository categoryRepository;
    private final ScrapRepository scrapRepository;
    private final NewsLikeRepository newsLikeRepository;
    private final NaverNewsRepository naverNewsRepository;

    public News findByNewsId(Long newsId) {
        return newsRepository.findById(newsId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
    }

    public NewsMainDataResponse getNewsMain() {
        LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
        List<News> topNews = newsRepository.findTopNewsByAdminOnly(threeDaysAgo, PageRequest.of(0, 10));
        List<PollListResponse> polls = pollService.getRecentList();

        return NewsMainDataResponse.builder()
                .newsList(NewsMainResponse.fromList(topNews))
                .pollList(polls)
                .build();
    }

    @Transactional
    public NewsResponse getNewsDetail(Long newsId, Long userId) {
        News news = findByNewsId(newsId);
        news.increaseView();
        int likeCount = newsLikeService.countLikeByNews(news);
        boolean liked = userId != null && newsLikeService.existsLike(newsId, userId);
        boolean scraped = userId != null && scrapService.existsScrap(newsId, userId);
        boolean followed = userId != null && followService.existsFollow(userId, news.getUser().getId());
        return NewsResponse.of(news, likeCount, liked, scraped, followed);
    }

    @Transactional
    public void addLike(Long newsId, Long userId) {
        News news = findByNewsId(newsId);
        if (newsLikeService.existsLike(newsId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        newsLikeService.addLike(news, userService.findByUserId(userId));
    }

    @Transactional
    public void removeLike(Long newsId, Long userId) {
        News news = findByNewsId(newsId);
        if (!newsLikeService.existsLike(newsId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        newsLikeService.removeLike(news, userService.findByUserId(userId));
    }

    @Transactional
    public void addScrap(Long newsId, Long userId) {
        News news = findByNewsId(newsId);
        if (scrapService.existsScrap(newsId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        scrapService.save(news, userService.findByUserId(userId));
    }

    @Transactional
    public void removeScrap(Long newsId, Long userId) {
        News news = findByNewsId(newsId);
        if (!scrapService.existsScrap(newsId, userId)) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }
        scrapService.deleteByUserAndNews(news, userService.findByUserId(userId));
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
        User user = userService.findByUserId(userId);
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

        String oldImageUrl = news.getImage();
        String defaultImageForCategory = getDefaultImageForCategoryId(categoryId);
        String newImageUrl = oldImageUrl;
        if (image != null && !image.isEmpty()) {
            try {
                byte[] imageBytes = image.getBytes();
                newImageUrl = fileUploaderService.uploadNewsImage(imageBytes, userId);

                if (oldImageUrl != null &&
                        !oldImageUrl.equals(defaultImageForCategory) &&
                        !oldImageUrl.isEmpty()) {
                    fileUploaderService.deleteFile(oldImageUrl);
                }
            } catch (IOException e) {
                throw new RuntimeException("이미지 업로드 실패", e);
            }
        }
        news.updateTitle(title);
        news.updateContent(content);
        news.updateImage(newImageUrl);
        news.updateCategory(category);
        newsSearchService.saveNews(news);
    }

    @Transactional
    public void deleteNews(Long userId, Long newsId) {
        News news = findByNewsId(newsId);
        scrapService.delete(news);
        fileUploaderService.deleteFile(news.getImage());
        newsRepository.delete(news);
        newsSearchService.deleteNews(newsId);
    }

    private byte[] resizeNewsImage(MultipartFile file) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Thumbnails.of(file.getInputStream())
                    .size(600, 350)
                    .outputFormat("jpg")
                    .outputQuality(0.9)
                    .toOutputStream(outputStream);
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException(ErrorCode.NOT_FOUND);
        }
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
        List<CategoryResponse> categories = categoryService.findAll();
        return NewsDetailInfoResponse.from(news, categories);
    }
}

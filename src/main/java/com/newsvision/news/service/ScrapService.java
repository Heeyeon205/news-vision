package com.newsvision.news.service;


import com.newsvision.news.entity.News;
import com.newsvision.news.entity.Scrap;
import com.newsvision.news.repository.ScrapRepository;
import com.newsvision.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ScrapService {
    private final ScrapRepository scrapRepository;

    public boolean existsScrap(Long newsId, Long userId) {
        return scrapRepository.existsByNewsIdAndUserId(newsId, userId);
    }

    public void save(News news, User user ) {
        Scrap scrap = Scrap.builder()
                .user(user)
                .news(news)
                .build();
        scrapRepository.save(scrap);
    }

    @Transactional
    public void delete(News news) {
        scrapRepository.deleteByNews(news);
    }

    @Transactional
    public void deleteByUserAndNews(News news, User user) {
        scrapRepository.deleteByUserAndNews(user, news);
    }

    public Page<Scrap> getMypageScrapList(Long id, Pageable pageable) {
        return scrapRepository.findByUserIdOrderByCreatedAtDesc(id, pageable);
    }
}

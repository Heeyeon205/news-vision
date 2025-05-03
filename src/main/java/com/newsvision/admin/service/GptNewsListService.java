package com.newsvision.admin.service;

import com.newsvision.admin.dto.response.GptNewsResponse;
import com.newsvision.news.entity.GptNews;
import com.newsvision.news.repository.GptNewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GptNewsListService {

    private final GptNewsRepository gptNewsRepository;

    @Autowired
    public GptNewsListService(GptNewsRepository gptNewsRepository) {
        this.gptNewsRepository = gptNewsRepository;
    }

    public List<GptNewsResponse> getAllGptNews() {
        List<GptNews> gptNewsList = gptNewsRepository.findAll();
        return gptNewsList.stream()
                .map(GptNewsResponse::of)
                .collect(Collectors.toList());
    }
    public List<GptNewsResponse> getMaxAllGptNews() {
        List<GptNews> gptNewsList = gptNewsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return gptNewsList.stream()
                .map(GptNewsResponse::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteGptNews(Long id) {
        GptNews gptNews = gptNewsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("GptNews not found with id: " + id));
        gptNewsRepository.delete(gptNews);
    }
}
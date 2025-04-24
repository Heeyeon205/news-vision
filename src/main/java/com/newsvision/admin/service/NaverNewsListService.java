package com.newsvision.admin.service;


import com.newsvision.admin.controller.response.NaverNewsResponse;
import com.newsvision.news.entity.NaverNews;
import com.newsvision.news.repository.NaverNewsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class NaverNewsListService {

   private final NaverNewsRepository naverNewsRepository;

  @Autowired
   public NaverNewsListService(NaverNewsRepository naverNewsRepository) {
       this.naverNewsRepository = naverNewsRepository;
    }

    public List<NaverNewsResponse> getAllNaverNews() {
        List<NaverNews> naverNewsList = naverNewsRepository.findAll();
        return naverNewsList.stream()
                .map(NaverNewsResponse::of)
                .collect(Collectors.toList());
    }

    public List<NaverNewsResponse> getMaxAllNaverNews() {
        List<NaverNews> naverNewsList = naverNewsRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        return naverNewsList.stream()
                .map(NaverNewsResponse::of)
                .collect(Collectors.toList());
    }


  @Transactional
   public void deleteNaverNews(Long id) {
        NaverNews naverNews = naverNewsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("NaverNews not found with id: " + id));
             naverNewsRepository.delete(naverNews);
   }
}
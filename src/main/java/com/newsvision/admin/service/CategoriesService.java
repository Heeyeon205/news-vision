package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoryRepository categoriesRepository;

    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll().stream()
                .map(cat -> CategoriesResponse.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build())
                .collect(Collectors.toList());
    }
}
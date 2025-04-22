package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.category.entity.Categories;
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

    // 카테고리 추가
    public Categories addCategory(String name) {
        Categories category = Categories.builder().name(name).build();
        return categoriesRepository.save(category);
    }

    // 카테고리 수정
    public Categories updateCategory(Long id, String name) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리 없음: " + id));
        category.setName(name);
        return categoriesRepository.save(category);
    }

    // 카테고리 삭제
    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
    }

}
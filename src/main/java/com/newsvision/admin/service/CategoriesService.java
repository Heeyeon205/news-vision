package com.newsvision.admin.service;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.board.repository.BoardRepository;
import com.newsvision.category.Categories;
import com.newsvision.category.CategoryRepository;
import com.newsvision.news.repository.NewsRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriesService {

    private final CategoryRepository categoriesRepository;
    private final NewsRepository newsRepository;
    private final BoardRepository boardRepository;

    public List<CategoriesResponse> getAllCategories() {
        return categoriesRepository.findAll().stream()
                .map(cat -> CategoriesResponse.builder()
                        .id(cat.getId())
                        .name(cat.getName())
                        .build())
                .collect(Collectors.toList());
    }



    public List<CategoriesResponse> getMaxAllCategories() {
        return categoriesRepository.findAll(Sort.by(Sort.Direction.DESC, "id")).stream()
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


    @Transactional
    public void deleteCategory(Long id) {
        Categories category = categoriesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));

        // news와 board의 category_id를 1로 업데이트
        newsRepository.updateCategoryIdToDefault(category.getId(), 1L);
        boardRepository.updateCategoryIdToDefault(category.getId(), 1L);

        // 카테고리 삭제
        categoriesRepository.delete(category);
    }
}
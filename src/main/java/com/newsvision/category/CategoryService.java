package com.newsvision.category;


import com.newsvision.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<CategoryResponse> findAll() {
        return categoryRepository.findAll().stream()
                .filter(categories -> categories.getId() != 1L)
                .map(categories -> new CategoryResponse(categories.getId(), categories.getName()))
                .toList();
    }
    public CategoryResponse create(CategoryRequest request) {
        Categories newCategory = Categories.builder()
                .name(request.getName())
                .build();
        Categories saved = categoryRepository.save(newCategory);
        return new CategoryResponse(saved.getId(), saved.getName());
    }

    public void delete(Long id) {
        if (id == 1L) {
            throw new IllegalArgumentException("기본 카테고리는 삭제할 수 없습니다.");
        }
        categoryRepository.deleteById(id);
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Categories category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("카테고리가 존재하지 않습니다."));

        category.setName(request.getName());
        Categories updated = categoryRepository.save(category);

        return new CategoryResponse(updated.getId(), updated.getName());
    }


}

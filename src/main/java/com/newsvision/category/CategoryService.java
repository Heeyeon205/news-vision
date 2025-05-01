package com.newsvision.category;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}

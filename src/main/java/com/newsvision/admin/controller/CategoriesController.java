package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.admin.service.CategoriesService;
import com.newsvision.category.Categories;
import com.newsvision.global.exception.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoriesController {

    private final CategoriesService categoriesService;
    private final CategoriesService categoryService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriesResponse>>> getCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoriesService.getAllCategories()));
    }


    @GetMapping("/max")
    public ResponseEntity<ApiResponse<List<CategoriesResponse>>> getMaxCategories() {
        return ResponseEntity.ok(ApiResponse.success(categoriesService.getMaxAllCategories()));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Categories>> addCategory(@RequestParam String name) {
        Categories category = categoryService.addCategory(name);
        return ResponseEntity.ok(ApiResponse.success(category));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Categories>> updateCategory(@PathVariable Long id, @RequestParam String name) {
        Categories updated = categoryService.updateCategory(id, name);
        return ResponseEntity.ok(ApiResponse.success(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}

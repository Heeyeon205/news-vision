package com.newsvision.admin.controller;

import com.newsvision.admin.controller.response.CategoriesResponse;
import com.newsvision.admin.service.CategoriesService;
import com.newsvision.category.entity.Categories;
import com.p6spy.engine.logging.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<CategoriesResponse>> getCategories() {
        return ResponseEntity.ok(categoriesService.getAllCategories());
    }


    @GetMapping("/max")
    public ResponseEntity<List<CategoriesResponse>> getMaxCategories() {
        return ResponseEntity.ok(categoriesService.getMaxAllCategories());
    }

    @PostMapping
    public ResponseEntity<Categories> addCategory(@RequestParam String name) {
        return ResponseEntity.ok(categoryService.addCategory(name));
    }


    @PutMapping("/{id}")
    public ResponseEntity<Categories> updateCategory(@PathVariable Long id, @RequestParam String name) {
        return ResponseEntity.ok(categoryService.updateCategory(id, name));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}

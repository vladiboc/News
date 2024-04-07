package org.example.news.web.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Category;
import org.example.news.mapper.v1.CategoryMapper;
import org.example.news.service.CategoryService;
import org.example.news.web.dto.category.CategoryListResponse;
import org.example.news.web.dto.category.CategoryResponse;
import org.example.news.web.dto.category.CategoryUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
public class CategoryController {
  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @GetMapping
  public ResponseEntity<CategoryListResponse> findAll() {
    final List<Category> categories = this.categoryService.findAll();
    final CategoryListResponse response = this.categoryMapper.categoryListToCategoryListResponse(categories);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable int id) {
    final Category category = this.categoryService.findById(id);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(category);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryUpsertRequest request) {
    final Category newCategory = this.categoryMapper.requestToCategory(request);
    final Category createdCategory = this.categoryService.save(newCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(createdCategory);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(@PathVariable int id, @RequestBody @Valid CategoryUpsertRequest request) {
    final Category editedCategory = this.categoryMapper.requestToCategory(request);
    final Category updatedCategory = this.categoryService.update(id, editedCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(updatedCategory);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.categoryService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

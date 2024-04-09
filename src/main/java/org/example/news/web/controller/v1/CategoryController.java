package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Category;
import org.example.news.mapper.v1.CategoryMapper;
import org.example.news.service.CategoryService;
import org.example.news.web.dto.category.CategoryListResponse;
import org.example.news.web.dto.category.CategoryResponse;
import org.example.news.web.dto.category.CategoryUpsertRequest;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "Категория 1.0", description = "Управление категориями новостей версия 1.0")
public class CategoryController {
  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @Operation(
      summary = "Получить список категорий",
      description = "Возвращает список категорий с номерами, названием, списком новостей",
      tags = {"Список"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryListResponse.class), mediaType = "application/json")}
  )
  @GetMapping
  public ResponseEntity<CategoryListResponse> findAll() {
    final List<Category> categories = this.categoryService.findAll();
    final CategoryListResponse response = this.categoryMapper.categoryListToCategoryListResponse(categories);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить категорию по номеру",
      description = "Возвращает номер категории, название, список новостей",
      tags = {"Номер"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable int id) {
    final Category category = this.categoryService.findById(id);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(category);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать категорию",
      description = "Возвращает номер созданной категории, название, номер новости и номер пользователя",
      tags = {"Создание"}
  )
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
  @PostMapping
  public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryUpsertRequest request) {
    final Category newCategory = this.categoryMapper.requestToCategory(request);
    final Category createdCategory = this.categoryService.save(newCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(createdCategory);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить категорию",
      description = "Возвращает номер обновленной категории, название, номер новости и номер пользователя",
      tags = {"Номер", "Обновление"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(@PathVariable int id, @RequestBody @Valid CategoryUpsertRequest request) {
    final Category editedCategory = this.categoryMapper.requestToCategory(request);
    final Category updatedCategory = this.categoryService.update(id, editedCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(updatedCategory);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Удалить категорию по номеру",
      description = "Удаляет категорию по номеру",
      tags = {"Номер", "Удаление"}
  )
  @ApiResponse(
      responseCode = "204"
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.categoryService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

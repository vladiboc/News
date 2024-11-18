package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.Category;
import org.example.news.mapper.v1.CategoryMapper;
import org.example.news.service.CategoryService;
import org.example.news.web.dto.category.CategoryFilter;
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
@Tag(name = "Category", description = "Управление категориями новостей")
public class CategoryController {
  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @Operation(
      summary = "Получить постраничный список категорий.",
      description = "Возвращает список категорий с идентификаторами, названием, списком новостей.<br>" +
      "Список выдается постранично. Размер страницы и текущий номер должен быть обязательно задан в параметрах запроса.",
      tags = {"Get"})
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryListResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping
  public ResponseEntity<CategoryListResponse> findAllByFilter(@Parameter(hidden = true) @Valid CategoryFilter filter) {
    final List<Category> categories = this.categoryService.findAllByFilter(filter);
    final CategoryListResponse response = this.categoryMapper.categoryListToCategoryListResponse(categories);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить категорию по идентификатору.",
      description = "Возвращает идентификатор категории, название, список новостей.",
      tags = {"Get"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable int id) {
    final Category category = this.categoryService.findById(id);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(category);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать категорию.",
      description = "Возвращает идентификатор созданной категории, название," +
          "идентификатор новости и идентификатор пользователя.",
      tags = {"Post"})
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @PostMapping
  public ResponseEntity<CategoryResponse> create(@RequestBody @Valid CategoryUpsertRequest request) {
    final Category newCategory = this.categoryMapper.requestToCategory(request);
    final Category createdCategory = this.categoryService.save(newCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(createdCategory);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить категорию.",
      description = "Возвращает идентификатор обновленной категории, название, идентификатор новости и идентификатор пользователя.",
      tags = {"Put"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @PutMapping("/{id}")
  public ResponseEntity<CategoryResponse> update(@PathVariable int id, @RequestBody @Valid CategoryUpsertRequest request) {
    final Category editedCategory = this.categoryMapper.requestToCategory(request);
    final Category updatedCategory = this.categoryService.update(id, editedCategory);
    final CategoryResponse response = this.categoryMapper.categoryToCategoryResponse(updatedCategory);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Удалить категорию по идентификатору.",
      description = "Удаляет категорию по идентификатору.",
      tags = {"Delete"})
  @ApiResponse(
      responseCode = "204")
  @Loggable
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.categoryService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

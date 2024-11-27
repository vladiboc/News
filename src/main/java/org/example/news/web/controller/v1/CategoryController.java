package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/category")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Управление категориями новостей")
@Loggable
public class CategoryController {
  private final CategoryService categoryService;
  private final CategoryMapper categoryMapper;

  @Operation(summary = "Получить постраничный список категорий.", description = "Возвращает список "
      + "категорий с идентификаторами, названием, списком новостей. Список выдается постранично. "
      + "Размер страницы и текущий номер должен быть обязательно задан в параметрах запроса.",
      security = @SecurityRequirement(name = "basicAuth"))
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CategoryListResponse.class),
      mediaType = "application/json")})
  @GetMapping
  public ResponseEntity<CategoryListResponse> findAllByFilter(
      @Parameter(hidden = true) @Valid CategoryFilter filter
  ) {
    final var categories = this.categoryService.findAllByFilter(filter);
    final var response = this.categoryMapper.categoryListToCategoryListResponse(categories);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Получить категорию по идентификатору.", description = "Возвращает "
      + "идентификатор категории, название, список новостей.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")})
  @ApiResponse(responseCode = "404", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @GetMapping("/{id}")
  public ResponseEntity<CategoryResponse> findById(@PathVariable int id) {
    final var category = this.categoryService.findById(id);
    final var response = this.categoryMapper.categoryToCategoryResponse(category);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Создать категорию. Разрешено если есть роль ADMIN или MODERATOR.",
      description = "Возвращает идентификатор созданной категории, название, идентификатор "
      + "новости и идентификатор пользователя.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "201", content = {@Content(
      schema = @Schema(implementation = CategoryResponse.class), mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<CategoryResponse> create(
      @RequestBody @Valid CategoryUpsertRequest request
  ) {
    final var newCategory = this.categoryMapper.requestToCategory(request);
    final var createdCategory = this.categoryService.save(newCategory);
    final var response = this.categoryMapper.categoryToCategoryResponse(createdCategory);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Обновить категорию. Разрешено только если есть роль ADMIN или MODERATOR.",
      description = "Возвращает идентификатор обновленной категории, название, идентификатор "
      + "новости и идентификатор пользователя.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CategoryResponse.class),
      mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class),
      mediaType = "application/json")})
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<CategoryResponse> update(
      @PathVariable int id, @RequestBody @Valid CategoryUpsertRequest request
  ) {
    final var editedCategory = this.categoryMapper.requestToCategory(request);
    final var updatedCategory = this.categoryService.update(id, editedCategory);
    final var response = this.categoryMapper.categoryToCategoryResponse(updatedCategory);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Удалить категорию по идентификатору. Разрешено только если есть роль ADMIN "
      + "или MODERATOR.", description = "Удаляет категорию по идентификатору.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(
      responseCode = "204")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.categoryService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

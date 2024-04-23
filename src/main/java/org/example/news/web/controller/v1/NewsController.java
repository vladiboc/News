package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.News;
import org.example.news.mapper.v1.NewsMapper;
import org.example.news.service.NewsService;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.example.news.web.dto.news.NewsFilter;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "Новость 1.0", description = "Управление новостями версия 1.0")
public class NewsController {
  private final NewsService newsService;
  private final NewsMapper newsMapper;

  @Operation(
      summary = "Получить список новостей",
      description = "Возвращает список новостей с номерами, заголовками, содержанием, номерами пользователей и категорий, списками комментариев",
      tags = {"Список"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = NewsListResponse.class), mediaType = "application/json")})
  @GetMapping("/all")
  public ResponseEntity<NewsListResponse> findAll() {
    final List<News> news = this.newsService.findAll();
    final NewsListResponse response = this.newsMapper.newsListToNewsListResponse(news);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить постраничный список новостей по заданному фильтру",
      description = "Возвращает список новостей с номерами, заголовками, содержанием, номерами пользователей и категорий, списками комментариев.<br>" +
          "Список выдается постранично. Размер страницы и текущий номер должен быть обязательно задан в параметрах запроса.<br>" +
          "Список отфильтрован по значениям, заданным в параметрах запроса. Все параметры необязательны, кроме размера и номера страницы.",
      tags = {"Список"})
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @Parameter(name = "userId", description = "Номер пользователя")
  @Parameter(name = "userName", description = "Имя пользователя")
  @Parameter(name = "categoryId", description = "Номер категории")
  @Parameter(name = "categoryName", description = "Имя категории")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = NewsListResponse.class), mediaType = "application/json")})
  @GetMapping
  public ResponseEntity<NewsListResponse> findAllByFilter(@Parameter(hidden = true) @Valid NewsFilter filter) {
    final List<News> news = this.newsService.findAllByFilter(filter);
    final NewsListResponse response = this.newsMapper.newsListToNewsListResponse(news);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить новость по номеру",
      description = "Возвращает номер новости, заголовок, содержание, номера пользователя и категории, списки комментариев",
      tags = {"Номер"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @GetMapping("/{id}")
  public ResponseEntity<NewsResponse> findById(@PathVariable int id) {
    final News news = this.newsService.findById(id);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(news);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать новость",
      description = "Возвращает номер созданной новости, заголовок, содержание, номер пользователя, номер категории, списки комментариев",
      tags = {"Создание"})
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsUpsertRequest request) {
    final News newNews = this.newsMapper.requestToNews(request);
    final News createdNews = this.newsService.save(newNews);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(createdNews);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить новость",
      description = "Возвращает номер обновленной новости, заголовок, содержание, номер пользователя, номер категории, списки комментариев",
      tags = {"Номер", "Обновление"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = NewsResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @PutMapping("/{id}")
  public ResponseEntity<NewsResponse> update(@PathVariable int id, @RequestBody @Valid NewsUpsertRequest request) {
    final News editedNews = this.newsMapper.requestToNews(request);
    final News updatedNews = this.newsService.update(id, editedNews);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(updatedNews);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Удалить новость по номеру",
      description = "Удаляет новость по номеру",
      tags = {"Номер", "Удаление"})
  @ApiResponse(
      responseCode = "204")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.newsService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

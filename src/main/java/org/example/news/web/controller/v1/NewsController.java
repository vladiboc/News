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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.News;
import org.example.news.db.entity.RoleType;
import org.example.news.mapper.v1.NewsMapper;
import org.example.news.security.AppUserPrincipal;
import org.example.news.service.NewsService;
import org.example.news.service.UserService;
import org.example.news.service.impl.NewsServiceImpl;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.example.news.web.dto.news.NewsFilter;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
@Tag(name = "News", description = "Управление новостями")
@Loggable
public class NewsController {
  private final NewsService newsService;
  private final NewsMapper newsMapper;

  @Operation(summary = "Получить постраничный список новостей по заданному фильтру.",
      description = "Возвращает список новостей с идентификаторами, заголовками, содержанием, "
          + "идентификаторами пользователей и категорий, списками комментариев. Список выдается "
          + "постранично. Размер страницы и текущий номер должен быть обязательно задан в "
          + " параметрах запроса. Список отфильтрован по значениям, заданным в параметрах запроса. "
          + "Все параметры необязательны, кроме размера и номера страницы.",
      security = @SecurityRequirement(name = "basicAuth"))
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @Parameter(name = "userId", description = "Идентификатор пользователя")
  @Parameter(name = "userName", description = "Имя пользователя")
  @Parameter(name = "categoryId", description = "Идентификатор категории")
  @Parameter(name = "categoryName", description = "Имя категории")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(
          implementation = NewsListResponse.class), mediaType = "application/json")})
  @GetMapping
  public ResponseEntity<NewsListResponse> findAllByFilter(
      @Parameter(hidden = true) @Valid NewsFilter filter
  ) {
    final var news = this.newsService.findAllByFilter(filter);
    final var response = this.newsMapper.newsListToNewsListResponse(news);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Получить новость по идентификатору.",
      description = "Возвращает идентификатор  новости, заголовок, содержание, идентификаторы "
          + "пользователя и категории, списки комментариев.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(
      responseCode = "200", content = {@Content(
          schema = @Schema(implementation = NewsResponse.class),
          mediaType = "application/json")})
  @ApiResponse(
      responseCode = "404", content = {@Content(
          schema = @Schema(implementation = ErrorMsgResponse.class),
          mediaType = "application/json")})
  @GetMapping("/{id}")
  public ResponseEntity<NewsResponse> findById(@PathVariable int id) {
    final var news = this.newsService.findById(id);
    final var response = this.newsMapper.newsToNewsResponse(news);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Создать новость.",
      description = "Возвращает идентификатор созданной новости, заголовок, содержание, "
          + "идентификатор пользователя, идентификатор категории, списки комментариев.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(
      responseCode = "201", content = {@Content(
          schema = @Schema(implementation = NewsResponse.class),
          mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400", content = {@Content(
          schema = @Schema(implementation = ErrorMsgResponse.class),
          mediaType = "application/json")})
  @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsUpsertRequest request) {
    final var newNews = this.newsMapper.requestToNews(request);
    final var createdNews = this.newsService.save(newNews);
    final var response = this.newsMapper.newsToNewsResponse(createdNews);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Обновить новость. Разрешено только пользователю-создателю новости.",
      description = "Возвращает идентификатор обновленной новости, заголовок, содержание, "
          + "идентификатор пользователя, идентификатор категории, списки комментариев.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(
      responseCode = "200", content = {@Content(
          schema = @Schema(implementation = NewsResponse.class),
          mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400", content = {@Content(
          schema = @Schema(implementation = ErrorMsgResponse.class),
          mediaType = "application/json")})
  @PutMapping("/{id}")
  public ResponseEntity<NewsResponse> update(
      @AuthenticationPrincipal UserDetails userDetails,
      @PathVariable int id,
      @RequestBody @Valid NewsUpsertRequest request
  ) {
    final var creatorId = this.newsService.findById(id).getUser().getId();
    final var requesterId = ((AppUserPrincipal) userDetails).getUserId();
    if (requesterId != creatorId) {
      throw new AuthorizationDeniedException("Unmatched user", () -> false);
    }
    final var editedNews = this.newsMapper.requestToNews(request);
    final var updatedNews = this.newsService.update(id, editedNews);
    final var response = this.newsMapper.newsToNewsResponse(updatedNews);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Удалить новость по идентификатору. Разрешено пользователю-создателю "
      + "новости. А также если есть роль ADMIN или MODERATOR.",
      description = "Удаляет новость по идентификатору.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(
      responseCode = "204")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(
      @AuthenticationPrincipal UserDetails userDetails, @PathVariable int id
  ) {
    final var roles =
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
    if (!roles.contains(RoleType.ROLE_ADMIN.name())
        && !roles.contains(RoleType.ROLE_MODERATOR.name())) {
      if (this.newsService.findById(id).getUser().getId()
          != ((AppUserPrincipal) userDetails).getUserId()) {
        throw new AuthorizationDeniedException("Unmatched user", () -> false);
      }
    }
    this.newsService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

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
import org.example.news.aop.loggable.Loggable;
import org.example.news.aop.matchable.MatchableCommentUser;
import org.example.news.db.entity.Comment;
import org.example.news.mapper.v1.CommentMapper;
import org.example.news.service.CommentService;
import org.example.news.web.dto.comment.CommentFilter;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "Комментарий 1.0", description = "Управление комментариями версия 1.0")
public class CommentController {
  private final CommentService commentService;
  private final CommentMapper commentMapper;

  @Operation(
      summary = "Получить список всех комментариев заданного пользователя.",
      description = "Возвращает список комментариев с номерами, содержимым, номером новости и номером пользователя.",
      tags = {"Список"})
  @Parameter(name = "userId", required = true, description = "Идентификатор пользователя")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CommentListResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping
  public ResponseEntity<CommentListResponse> findAllByFilter(@Parameter(hidden = true) @Valid CommentFilter filter) {
    final List<Comment> comments = this.commentService.findAllByFilter(filter);
    final CommentListResponse response = this.commentMapper.commentListToCommentListResponse(comments);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить комментарий по номеру.",
      description = "Возвращает номер комментария, содержание, номер новости и номер пользователя.",
      tags = {"Номер"})
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> findById(@PathVariable int id) {
    final Comment comment = this.commentService.findById(id);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(comment);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать комментарий.",
      description = "Возвращает номер созданного комментария, содержание, номер новости и номер пользователя.",
      tags = {"Создание"})
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @PostMapping
  public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentUpsertRequest request) {
    final Comment newComment = this.commentMapper.requestToComment(request);
    final Comment createdComment = this.commentService.save(newComment);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(createdComment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить комментарий. Разрешено только пользователю-создателю комментария. " +
          "Идентификатор пользователя-создателя принимается через http-заголовок.",
      description = "Возвращает номер обновленного комментария, содержание, номер новости и номер пользователя.",
      tags = {"Номер", "Обновление"})
  @Parameter(name = "X-User-Id", in = ParameterIn.HEADER, required = true, description = "Идентификатор пользователя-создателя новости")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = CommentResponse.class), mediaType = "application/json")})
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @Loggable
  @MatchableCommentUser
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> update(@PathVariable int id, @RequestBody @Valid CommentUpsertRequest request) {
    final Comment editedComment = this.commentMapper.requestToComment(request);
    final Comment updatedComment = this.commentService.update(id, editedComment);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(updatedComment);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Удалить комментарий по номеру. Разрешено только пользователю-создателю комментария. " +
          "Идентификатор пользователя-создателя принимается через http-заголовок.",
      description = "Удаляет комментарий по номеру.",
      tags = {"Номер", "Удаление"})
  @Parameter(name = "X-User-Id", in = ParameterIn.HEADER, required = true, description = "Идентификатор пользователя-создателя новости")
  @ApiResponse(
      responseCode = "204")
  @Loggable
  @MatchableCommentUser
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.commentService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

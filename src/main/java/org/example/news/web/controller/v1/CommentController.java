package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
@Tag(name = "Comment", description = "Управление комментариями")
@Loggable
public class CommentController {
  private final CommentService commentService;
  private final CommentMapper commentMapper;

  @Operation(summary = "Получить список всех комментариев заданного пользователя.", description =
      "Возвращает список комментариев с идентификаторами, содержимым, идентификатором новости и "
      + "идентификатором пользователя.")
  @Parameter(name = "userId", required = true, description = "Идентификатор пользователя")
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CommentListResponse.class),
      mediaType = "application/json")})
  @GetMapping
  public ResponseEntity<CommentListResponse> findAllByFilter(
      @Parameter(hidden = true) @Valid CommentFilter filter
  ) {
    final List<Comment> comments = this.commentService.findAllByFilter(filter);
    final CommentListResponse response =
        this.commentMapper.commentListToCommentListResponse(comments);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Получить комментарий по идентификатору.", description = "Возвращает "
      + "идентификатор комментария, содержание, идентификатор новости и идентификатор "
      + "пользователя.")
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CommentResponse.class),
      mediaType = "application/json")})
  @ApiResponse(responseCode = "404", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class),
      mediaType = "application/json")})
  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> findById(@PathVariable int id) {
    final Comment comment = this.commentService.findById(id);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(comment);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Создать комментарий.", description = "Возвращает идентификатор созданного "
      + "комментария, содержание, идентификатор новости и идентификатор пользователя.")
  @ApiResponse(responseCode = "201", content = {@Content(
      schema = @Schema(implementation = CommentResponse.class),
      mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class),
      mediaType = "application/json")})
  @PostMapping
  public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentUpsertRequest request) {
    final Comment newComment = this.commentMapper.requestToComment(request);
    final Comment createdComment = this.commentService.save(newComment);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(createdComment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "Обновить комментарий. Разрешено только пользователю-создателю комментария. "
      + "Идентификатор пользователя-создателя принимается через http-заголовок.",
      description = "Возвращает идентификатор обновленного комментария, содержание, идентификатор "
      + "новости и идентификатор пользователя.")
  @Parameter(name = "X-User-Id", in = ParameterIn.HEADER, required = true,
      description = "Идентификатор пользователя-создателя новости")
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = CommentResponse.class),
      mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class),
      mediaType = "application/json")})
  @MatchableCommentUser
  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> update(
      @PathVariable int id, @RequestBody @Valid CommentUpsertRequest request
  ) {
    final Comment editedComment = this.commentMapper.requestToComment(request);
    final Comment updatedComment = this.commentService.update(id, editedComment);
    final CommentResponse response = this.commentMapper.commentToCommentResponse(updatedComment);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Удалить комментарий по идентификатору. Разрешено только "
      + "пользователю-создателю комментария. Идентификатор пользователя-создателя принимается "
      + "через http-заголовок.", description = "Удаляет комментарий по номеру.")
  @Parameter(name = "X-User-Id", in = ParameterIn.HEADER, required = true,
      description = "Идентификатор пользователя-создателя новости")
  @ApiResponse(responseCode = "204")
  @MatchableCommentUser
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.commentService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

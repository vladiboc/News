package org.example.news.web.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Comment;
import org.example.news.mapper.v1.CommentMapper;
import org.example.news.service.CommentService;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
  private final CommentService commentService;
  private final CommentMapper commentMapper;

  @GetMapping
  public ResponseEntity<CommentListResponse> findAll() {
    final List<Comment> comments = this.commentService.findAll();
    final CommentListResponse response = this.commentMapper.commentListToCommentListResponse(comments);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<CommentResponse> findById(@PathVariable int id) {
    final Comment comment = this.commentService.findById(id);
    final CommentResponse response = this.commentMapper.commentToResponse(comment);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<CommentResponse> create(@RequestBody @Valid CommentUpsertRequest request) {
    final Comment newComment = this.commentMapper.requestToComment(request);
    final Comment createdComment = this.commentService.save(newComment);
    final CommentResponse response = this.commentMapper.commentToResponse(createdComment);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<CommentResponse> update(@PathVariable int id, @RequestBody @Valid CommentUpsertRequest request) {
    final Comment editedComment = this.commentMapper.requestToComment(request);
    final Comment updatedComment = this.commentService.update(id, editedComment);
    final CommentResponse response = this.commentMapper.commentToResponse(updatedComment);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.commentService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

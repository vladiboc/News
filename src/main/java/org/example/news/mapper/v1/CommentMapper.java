package org.example.news.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Comment;
import org.example.news.service.UserService;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CommentMapper {
  private final UserService userService;
  public Comment requestToComment(CommentUpsertRequest request) {
    Comment comment = new Comment();
    comment.setContent(request.getContent());
    comment.setUser(userService.findById(request.getUserId()));
    return comment;
  }

  public CommentResponse commentToResponse(Comment comment) {
    CommentResponse commentResponse = new CommentResponse();
    commentResponse.setId(comment.getId());
    commentResponse.setContent(comment.getContent());
    commentResponse.setUserId(comment.getUser().getId());
    return commentResponse;
  }

  public CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
    CommentListResponse response = new CommentListResponse();
    response.setComments(this.commentListToListOfCommentResponse(comments));
    return response;
  }

  public List<CommentResponse> commentListToListOfCommentResponse(List<Comment> comments) {
    return comments.stream()
        .map(this::commentToCommentResponse)
        .collect(Collectors.toList());
  }

  private CommentResponse commentToCommentResponse(Comment comment) {
    CommentResponse commentResponse = new CommentResponse();
    commentResponse.setId(comment.getId());
    commentResponse.setContent(comment.getContent());
    commentResponse.setUserId(comment.getUser().getId());
    return commentResponse;
  }
}

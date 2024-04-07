package org.example.news.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Comment;
import org.example.news.service.NewsService;
import org.example.news.service.UserService;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentMapper {
  private final NewsService newsService;
  private final UserService userService;

  public Comment requestToComment(CommentUpsertRequest request) {
    Comment comment = new Comment(request.getContent());

    comment.setNews(newsService.findById(request.getNewsId()));
    comment.setUser(userService.findById(request.getUserId()));

    return comment;
  }

  public CommentResponse commentToCommentResponse(Comment comment) {
    return new CommentResponse(
        comment.getId(),
        comment.getContent(),
        comment.getNews().getId(),
        comment.getUser().getId()
    );
  }

  public CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
    List<CommentResponse> commentResponses = this.commentListToListOfCommentResponse(comments);
    return new CommentListResponse(commentResponses);
  }

  public List<CommentResponse> commentListToListOfCommentResponse(List<Comment> comments) {
    return comments.stream()
        .map(this::commentToCommentResponse)
        .toList();
  }
}

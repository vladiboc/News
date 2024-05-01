package org.example.news.mapper.v1;

import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.Comment;
import org.example.news.service.NewsService;
import org.example.news.service.UserService;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommentMapperDelegate implements CommentMapper {
  @Autowired
  private UserService userService;
  @Autowired
  private NewsService newsService;

  @Loggable
  @Override
  public Comment requestToComment(CommentUpsertRequest request) {
    final Comment comment = new Comment(request.getContent());

    comment.setNews(this.newsService.findById(request.getNewsId()));
    comment.setUser(this.userService.findById(request.getUserId()));

    return comment;
  }
}

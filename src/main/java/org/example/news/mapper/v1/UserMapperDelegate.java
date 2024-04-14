package org.example.news.mapper.v1;

import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.example.news.web.dto.user.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class UserMapperDelegate implements UserMapper {
  @Autowired
  NewsMapper newsMapper;
  @Autowired
  CommentMapper commentMapper;

  @Override
  public UserResponse userToUserResponse(User user) {
    UserResponse userResponse = new UserResponse(user.getId(), user.getName());

    List<News> news = user.getNews();
    List<NewsResponseForList> newsResponses = this.newsMapper.newsListToListOfNewsResponse(news);
    userResponse.setNews(newsResponses);

    List<Comment> comments = user.getComments();
    List<CommentResponse> commentResponses = this.commentMapper.commentListToListOfCommentResponse(comments);
    userResponse.setComments(commentResponses);

    return userResponse;
  }
}

package org.example.news.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserResponseForList;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper {
  private final NewsMapper newsMapper;
  private final CommentMapperV0 commentMapper;

  public User requestToUser(UserUpsertRequest request) {
    return new User(request.getName());
  }

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

  // TODO  получать из сервиса сразу List<UserForList> суммировать в запросе
  public UserListResponse userListToUserListResponse(List<User> users) {
    List<UserResponseForList> userResponses = users.stream()
        .map(this::userToUserResponseForList)
        .toList();
    return new UserListResponse(userResponses);
  }

  private UserResponseForList userToUserResponseForList(User user) {
    return new UserResponseForList(
        user.getId(),
        user.getName(),
        user.getNews().size(),
        user.getComments().size()
    );
  }
}

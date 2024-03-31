package org.example.news.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.User;
import org.example.news.web.dto.user.UserResponseForList;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserMapper {
  private final CommentMapper commentMapper;

  public User requestToUser(UserUpsertRequest request) {
    User newUser = new User(request.getName());
    return newUser;
  }

  public UserResponse userToResponse(User user) {
    UserResponse userResponse = new UserResponse();
    userResponse.setId(user.getId());
    userResponse.setName(user.getName());
    userResponse.setComments(commentMapper.commentListToListOfCommentResponse(user.getComments()));
    return userResponse;
  }

  // TODO  получать из сервиса сразу List<UserForList> суммировать в запросе
  public UserListResponse userListToUserListResponse(List<User> users) {
    UserListResponse response = new UserListResponse();
    response.setUsers(users.stream()
        .map(this::userToUserForList)
        .collect(Collectors.toList())
    );
    return response;
  }

  private UserResponseForList userToUserForList(User user) {
    UserResponseForList userForList = new UserResponseForList();
    userForList.setId(user.getId());
    userForList.setName(user.getName());
    userForList.setCommentsCount(user.getComments().size());
    return userForList;
  }
}

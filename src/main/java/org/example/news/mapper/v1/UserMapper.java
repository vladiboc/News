package org.example.news.mapper.v1;

import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.User;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserResponseForList;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Loggable
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CommentMapper.class, NewsMapper.class})
public interface UserMapper {
  User requestToUser(UserUpsertRequest request);

  UserResponse userToUserResponse(User user);

  default UserResponseForList userToUserResponseForList(User user) {
    return new UserResponseForList(
        user.getId(),
        user.getName(),
        user.getNews().size(),
        user.getComments().size()
    );
  }

  List<UserResponseForList> userListToListOfUserResponseForList(List<User> users);

  default UserListResponse userListToUserListResponse(List<User> users) {
    return new UserListResponse(this.userListToListOfUserResponseForList(users));
  }
}

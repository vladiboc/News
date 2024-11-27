package org.example.news.mapper.v1;

import java.util.List;
import java.util.stream.Collectors;
import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.Role;
import org.example.news.db.entity.User;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserResponseForList;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Loggable
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {CommentMapper.class, NewsMapper.class, RoleMapper.class})
public interface UserMapper {
  default User requestToUser(UserUpsertRequest request) {
    return new User(
        request.getName(),
        request.getPassword(),
        request.getRoles().stream().map(Role::from).collect(Collectors.toList())
    );
  }

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

package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
import org.example.news.util.TestStringUtil;
import org.example.news.web.controller.core.AbstractControllerTest;
import org.example.news.web.dto.user.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


class UserControllerTest extends AbstractControllerTest {
  @MockBean
  private UserService userService;
  @MockBean
  private UserMapper userMapper;

  @Test
  void whenFindAllByFilter_thenReturnAllUsersByFilter() throws Exception {
    final List<User> users = new ArrayList<>();
    users.add(new User(1, "Пользователь №1"));
    users.add(new User(2, "Пользователь №2"));
    final UserFilter filter = new UserFilter(3, 0);

    final List<UserResponseForList> userResponses = new ArrayList<>();
    userResponses.add(new UserResponseForList(1, "Пользователь №1", 1, 0));
    userResponses.add(new UserResponseForList(2, "Пользователь №2", 0,1));

    final UserListResponse userListResponse = new UserListResponse(userResponses);

    Mockito.when(this.userService.findAllByFilter(filter)).thenReturn(users);
    Mockito.when(this.userMapper.userListToUserListResponse(users)).thenReturn(userListResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/find_all_users_response.json");
    final String actualResponse = this.mockGet("/api/v1/user?pageSize=3&pageNumber=0", HttpStatus.OK);

    Mockito.verify(this.userService, Mockito.times(1)).findAllByFilter(filter);
    Mockito.verify(this.userMapper, Mockito.times(1)).userListToUserListResponse(users);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenFindById_thenReturnUserById() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final UserResponse userResponse = new UserResponse(1, "Пользователь №1");

    Mockito.when(this.userService.findById(1)).thenReturn(user);
    Mockito.when(this.userMapper.userToUserResponse(user)).thenReturn(userResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/find_user_by_id_response.json");
    final String actualResponse = this.mockGet("/api/v1/user/1", HttpStatus.OK);

    Mockito.verify(this.userService, Mockito.times(1)).findById(1);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToUserResponse(user);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreate_thenReturnNewUser() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest("Пользователь №1");
    final User newUser = new User("Пользователь №1");
    final User createdUser = new User(1, "Пользователь №1");
    final UserResponse response = new UserResponse(1, "Пользователь №1");

    Mockito.when(this.userMapper.requestToUser(request)).thenReturn(newUser);
    Mockito.when(this.userService.save(newUser)).thenReturn(createdUser);
    Mockito.when(this.userMapper.userToUserResponse(createdUser)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/create_user_response.json");
    final String actualResponse = this.mockPost("/api/v1/user", request, HttpStatus.CREATED);

    Mockito.verify(this.userMapper, Mockito.times(1)).requestToUser(request);
    Mockito.verify(this.userService, Mockito.times(1)).save(newUser);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToUserResponse(createdUser);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenUpdate_thenReturnUpdatedUser() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest("Пользователь №1 обновленный");
    final User editedUser = new User("Пользователь №1 обновленный");
    final User updatedUser = new User(1, "Пользователь №1 обновленный");
    final UserResponse response = new UserResponse(1, "Пользователь №1 обновленный");

    Mockito.when(this.userMapper.requestToUser(request)).thenReturn(editedUser);
    Mockito.when(this.userService.update(1, editedUser)).thenReturn(updatedUser);
    Mockito.when(this.userMapper.userToUserResponse(updatedUser)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/update_user_response.json");
    final String actualResponse = this.mockPut("/api/v1/user/1", request, HttpStatus.OK);

    Mockito.verify(this.userMapper, Mockito.times(1)).requestToUser(request);
    Mockito.verify(this.userService, Mockito.times(1)).update(1, editedUser);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToUserResponse(updatedUser);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenDeleteById_thenReturnNoContent() throws Exception {
    this.mockDelete("/api/v1/user/1", HttpStatus.NO_CONTENT);

    Mockito.verify(this.userService, Mockito.times(1)).deleteById(1);
  }

  @Test
  void whenFindByIdNotExistedUser_thenReturnError() throws Exception {
    Mockito.when(this.userService.findById(999)).thenThrow(new EntityNotFoundException("Пользователь с id 999 не найден!"));

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/_err_user_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/user/999", HttpStatus.NOT_FOUND);

    Mockito.verify(this.userService, Mockito.times(1)).findById(999);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateUserWithEmptyName_thenReturnError() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest();
    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/_err_empty_user_name_response.json");
    final String actualResponse = this.mockPost("/api/v1/user", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @ParameterizedTest
  @MethodSource("illegalSizeName")
  void whenCreateUserWithIllegalSizeName_thenReturnError(String nameOfIllegalSize) throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest(nameOfIllegalSize);
    final String expectedResponse = TestStringUtil.readStringFromResource( "response/user/_err_user_name_illegal_size_response.json");
    final String actualResponse = mockPost("/api/v1/user", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  private static Stream<Arguments> illegalSizeName() {
    return Stream.of(
        Arguments.of(RandomString.make(1)),
        Arguments.of(RandomString.make(33))
    );
  }
}

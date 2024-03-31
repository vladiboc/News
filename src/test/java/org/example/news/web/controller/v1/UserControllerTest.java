package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
import org.example.news.util.ErrorMsg;
import org.example.news.util.TestStringUtil;
import org.example.news.web.controller.core.AbstractControllerTest;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserResponseForList;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class UserControllerTest extends AbstractControllerTest {
  @MockBean
  private UserService userService;
  @MockBean
  private UserMapper userMapper;

  @Test
  void whenFindAll_thenReturnAllUsers() throws Exception {
    final List<User> users = new ArrayList<>();
    users.add(new User(1, "Пользователь №1"));
    users.add(new User(2, "Пользователь №2"));

    final List<UserResponseForList> usersForLists = new ArrayList<>();
    usersForLists.add(new UserResponseForList(1, "Пользователь №1", 0));
    usersForLists.add(new UserResponseForList(2, "Пользователь №2", 1));

    final UserListResponse userListResponse = new UserListResponse(usersForLists);

    Mockito.when(this.userService.findAll()).thenReturn(users);
    Mockito.when(this.userMapper.userListToUserListResponse(users)).thenReturn(userListResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/find_all_users_response.json");
    final String actualResponse = this.mockGet("/api/v1/user", HttpStatus.OK);

    Mockito.verify(this.userService, Mockito.times(1)).findAll();
    Mockito.verify(this.userMapper, Mockito.times(1)).userListToUserListResponse(users);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenFindById_thenReturnUserById() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final UserResponse userResponse = new UserResponse(1, "Пользователь №1");

    Mockito.when(this.userService.findById(1)).thenReturn(user);
    Mockito.when(this.userMapper.userToResponse(user)).thenReturn(userResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/find_user_by_id_response.json");
    final String actualResponse = this.mockGet("/api/v1/user/1", HttpStatus.OK);

    Mockito.verify(this.userService, Mockito.times(1)).findById(1);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToResponse(user);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreate_thenReturnNewUser() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest("Пользователь №1");
    final User requestedUser = new User("Пользователь №1");
    final User createdUser = new User(1, "Пользователь №1");
    final UserResponse response = new UserResponse(1, "Пользователь №1");

    Mockito.when(this.userMapper.requestToUser(request)).thenReturn(requestedUser);
    Mockito.when(this.userService.save(requestedUser)).thenReturn(createdUser);
    Mockito.when(this.userMapper.userToResponse(createdUser)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/create_user_response.json");
    final String actualResponse = this.mockPost("/api/v1/user", request, HttpStatus.CREATED);

    Mockito.verify(this.userMapper, Mockito.times(1)).requestToUser(request);
    Mockito.verify(this.userService, Mockito.times(1)).save(requestedUser);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToResponse(createdUser);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenUpdateUser_thenReturnUpdatedUser() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest("Пользователь №1 обновленный");
    final User editedUser = new User(1, "Пользователь №1 обновленный");
    final User updatedUser = new User(1, "Пользователь №1 обновленный");
    final UserResponse response = new UserResponse(1, "Пользователь №1 обновленный");

    Mockito.when(this.userMapper.requestToUser(request)).thenReturn(editedUser);
    Mockito.when(this.userService.update(1, editedUser)).thenReturn(updatedUser);
    Mockito.when(this.userMapper.userToResponse(updatedUser)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/user/update_user_response.json");
    final String actualResponse = this.mockPut("/api/v1/user/1", request, HttpStatus.OK);

    Mockito.verify(this.userMapper, Mockito.times(1)).requestToUser(request);
    Mockito.verify(this.userService, Mockito.times(1)).update(1, editedUser);
    Mockito.verify(this.userMapper, Mockito.times(1)).userToResponse(updatedUser);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenDeleteUserById_thenReturnNoContent() throws Exception {
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

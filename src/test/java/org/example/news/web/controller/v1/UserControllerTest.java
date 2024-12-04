package org.example.news.web.controller.v1;

import java.util.Set;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.RoleType;
import org.example.news.util.TestStringUtil;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;


class UserControllerTest extends AbstractControllerTest {

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenFindAllByFilter_thenReturnAllUsersByFilter() throws Exception {
    final var expectedResponseString =
        TestStringUtil.readStringFromResource("response/user/find_all_users_response.json");
    final var expectedResponse =
        this.objectMapper.readValue(expectedResponseString, UserListResponse.class);
    final var actualResponse = this.objectMapper.readValue(this.mockGet(
        "/api/v1/user?pageSize=3&pageNumber=0", HttpStatus.OK), UserListResponse.class);

    final var expectedUsers = expectedResponse.getUsers();
    final var actualUsers = actualResponse.getUsers();
    IntStream.range(0, expectedUsers.size()).forEach(i -> {
      Assertions.assertEquals(expectedUsers.get(i).getName(), actualUsers.get(i).getName());
      Assertions.assertEquals(expectedUsers.get(i).getNewsCount(), actualUsers.get(i).getNewsCount());
      Assertions.assertEquals(expectedUsers.get(i).getCommentsCount(), actualUsers.get(i).getCommentsCount());
    });
  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
      value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void whenFindById_thenReturnUserById() throws Exception {
    final var id = this.userService.findByName("testUser").getId();
    final var expectedResponseString =
        TestStringUtil.readStringFromResource("response/user/find_user_by_id_response.json");
    final var expectedResponse =
        this.objectMapper.readValue(expectedResponseString, UserResponse.class);
    final var actualResponse = this.objectMapper.readValue(
        this.mockGet("/api/v1/user/" + id, HttpStatus.OK), UserResponse.class);

    Assertions.assertEquals(expectedResponse.getName(), actualResponse.getName());
    Assertions.assertEquals(expectedResponse.getRoles(), actualResponse.getRoles());
    Assertions.assertEquals(expectedResponse.getNews(), actualResponse.getNews());
    Assertions.assertEquals(expectedResponse.getComments(), actualResponse.getComments());
  }

  @Test
  void whenCreate_thenReturnNewUser() throws Exception {
    final UserUpsertRequest request = new UserUpsertRequest(
        "Пользователь №1", "12345", Set.of(RoleType.ROLE_USER));

    final var expectedResponseString =
        TestStringUtil.readStringFromResource("response/user/create_user_response.json");
    final var expectedResponse =
        this.objectMapper.readValue(expectedResponseString, UserResponse.class);
    final var actualResponse = this.objectMapper.readValue(
        this.mockPost("/api/v1/user", request, HttpStatus.CREATED), UserResponse.class);

    Assertions.assertEquals(expectedResponse.getName(), actualResponse.getName());
    Assertions.assertEquals(expectedResponse.getRoles(), actualResponse.getRoles());
    Assertions.assertEquals(expectedResponse.getNews(), actualResponse.getNews());
    Assertions.assertEquals(expectedResponse.getComments(), actualResponse.getComments());
  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
      value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void whenUpdate_thenReturnUpdatedUser() throws Exception {
    final var id = this.userService.findByName("testUser").getId();
    final UserUpsertRequest request = new UserUpsertRequest(
        "Пользователь №1 обновленный", "1234", Set.of(RoleType.ROLE_ADMIN));
    final String expectedResponseString =
        TestStringUtil.readStringFromResource("response/user/update_user_response.json");
    final var expectedResponse =
        this.objectMapper.readValue(expectedResponseString, UserResponse.class);
    final var actualResponse = this.objectMapper.readValue(
        this.mockPut("/api/v1/user/" + id, request, HttpStatus.OK), UserResponse.class);

    Assertions.assertEquals(expectedResponse.getName(), actualResponse.getName());
    Assertions.assertEquals(expectedResponse.getRoles(), actualResponse.getRoles());
    Assertions.assertEquals(expectedResponse.getNews(), actualResponse.getNews());
    Assertions.assertEquals(expectedResponse.getComments(), actualResponse.getComments());
  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
      value = "testAdmin", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void whenDeleteById_thenReturnNoContent() throws Exception {
    this.mockDelete("/api/v1/user/1", HttpStatus.NO_CONTENT);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenFindByIdNotExistedUser_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource(
        "response/user/_err_user_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/user/999", HttpStatus.NOT_FOUND);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateUserWithEmptyName_thenReturnError() throws Exception {
    final UserUpsertRequest request =
        new UserUpsertRequest(null, "123", Set.of(RoleType.ROLE_ADMIN));
    final String expectedResponse =
        TestStringUtil.readStringFromResource("response/user/_err_empty_user_name_response.json");
    final String actualResponse = this.mockPost("/api/v1/user", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @ParameterizedTest
  @MethodSource("illegalSizeName")
  void whenCreateUserWithIllegalSizeName_thenReturnError(
      final String nameOfIllegalSize
  ) throws Exception {
    final UserUpsertRequest request =
        new UserUpsertRequest(nameOfIllegalSize, "1234", Set.of(RoleType.ROLE_USER));
    final String expectedResponse = TestStringUtil.readStringFromResource(
        "response/user/_err_user_name_illegal_size_response.json");
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

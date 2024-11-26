package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.news.aop.loggable.Loggable;
import org.example.news.aop.matchable.MatchableUser;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.example.news.web.dto.user.UserFilter;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Loggable
@RequiredArgsConstructor
@Tag(name = "User", description = "Управление пользователями")
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @Operation(summary = "Получить постраничный список пользователей.", description = "Возвращает "
      + "список пользователей с идентификаторами, именами, количеством созданных новостей и "
      + "комментариев.<br>Список выдается постранично. Размер страницы и текущий номер должен быть "
      + "обязательно задан в параметрах запроса.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = UserListResponse.class),
      mediaType = "application/json")})
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @PreAuthorize("hasRole('ADMIN')")
  @GetMapping
  public ResponseEntity<UserListResponse> findAllByFilter(
      @Parameter(hidden = true) @Valid final UserFilter filter
  ) {
    final List<User> users = this.userService.findAllByFilter(filter);
    final UserListResponse response = this.userMapper.userListToUserListResponse(users);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Получить пользователя по идентификатору.", description = "Возвращает "
      + "идентификатор пользователя, имя пользователя, список созданных новостей, список созданных "
      + "комментариев.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "200", content = {@Content(
          schema = @Schema(implementation = UserResponse.class),
          mediaType = "application/json")})
  @ApiResponse(responseCode = "404", content = {@Content(
          schema = @Schema(implementation = ErrorMsgResponse.class),
          mediaType = "application/json")})
  @MatchableUser
  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable final int id) {
    final User foundUser = this.userService.findById(id);
    final UserResponse response = this.userMapper.userToUserResponse(foundUser);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Создать пользователя.", description = "Возвращает идентификатор созданного"
      + " пользователя, имя пользователя, пустые списки созданных новостей и комментариев.")
  @ApiResponse(responseCode = "201", content = {@Content(
      schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @MatchableUser
  @PostMapping
  public ResponseEntity<UserResponse> create(@RequestBody @Valid final UserUpsertRequest request) {
    final User newUser = this.userMapper.requestToUser(request);
    final User savedUser = this.userService.createNewUser(newUser);
    final UserResponse response = this.userMapper.userToUserResponse(savedUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation( summary = "Обновить пользователя с заданным идентификатором.", description =
      "Возвращает идентификатор обновленного пользователя, имя пользователя, списки созданных "
      + "новостей и комментариев.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "200", content = {@Content(
      schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")})
  @ApiResponse(responseCode = "400", content = {@Content(
      schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")})
  @MatchableUser
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(
      @PathVariable int id, @RequestBody @Valid final UserUpsertRequest request
  ) {
    final User editedUser = this.userMapper.requestToUser(request);
    final User updatedUser = this.userService.update(id, editedUser);
    final UserResponse response = this.userMapper.userToUserResponse(updatedUser);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "Удалить пользователя по идентификатору.", description = "Удаляет "
      + "пользователя по идентификатору.",
      security = @SecurityRequirement(name = "basicAuth"))
  @ApiResponse(responseCode = "204")
  @MatchableUser
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable final int id) {
    this.userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

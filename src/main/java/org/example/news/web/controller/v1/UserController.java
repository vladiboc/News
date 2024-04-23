package org.example.news.web.controller.v1;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.user.UserFilter;
import org.example.news.web.dto.user.UserListResponse;
import org.example.news.web.dto.user.UserResponse;
import org.example.news.web.dto.user.UserUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "Пользователь 1.0", description = "Управление пользователями 1.0")
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @Operation(
      summary = "Получить список пользователей",
      description = "Возвращает список пользователей с номерами, именами, количеством созданных новостей и комментариев",
      tags = {"Список"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = UserListResponse.class), mediaType = "application/json")}
  )
  @GetMapping("/all")
  public ResponseEntity<UserListResponse> findAll() {
    final List<User> allUsers = this.userService.findAll();
    final UserListResponse response = this.userMapper.userListToUserListResponse(allUsers);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить постраничный список пользователей.",
      description = "Возвращает список пользователей с номерами, именами, количеством созданных новостей и комментариев.<br>" +
          "Список выдается постранично. Размер страницы и текущий номер должен быть обязательно задан в параметрах запроса.",
      tags = {"Список"})
  @Parameter(name = "pageSize", required = true, description = "Размер страницы получаемых данных")
  @Parameter(name = "pageNumber", required = true, description = "Номер страницы получаемых данных")
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = UserListResponse.class), mediaType = "application/json")})
  @GetMapping
  public ResponseEntity<UserListResponse> findAllByFilter(@Parameter(hidden = true) @Valid UserFilter filter) {
    final List<User> users = this.userService.findAllByFilter(filter);
    final UserListResponse response = this.userMapper.userListToUserListResponse(users);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Получить пользователя по номеру",
      description = "Возвращает номер пользователя, имя пользователя, список созданных новостей, список созданных комментариев",
      tags = {"Номер"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "404",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
@GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable int id) {
    final User foundUser = this.userService.findById(id);
    final UserResponse response = this.userMapper.userToUserResponse(foundUser);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Создать пользователя",
      description = "Возвращает номер созданного пользователя, имя пользователя, пустые списки созданных новостей и комментариев",
      tags = {"Создание"}
  )
  @ApiResponse(
      responseCode = "201",
      content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
  @PostMapping
  public ResponseEntity<UserResponse> create(@RequestBody @Valid UserUpsertRequest request) {
    final User newUser = this.userMapper.requestToUser(request);
    final User savedUser = this.userService.save(newUser);
    final UserResponse response = this.userMapper.userToUserResponse(savedUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(
      summary = "Обновить пользователя с заданным номером",
      description = "Возвращает номер обновленного пользователя, имя пользователя, списки созданных новостей и комментариев",
      tags = {"Номер", "Обновление"}
  )
  @ApiResponse(
      responseCode = "200",
      content = {@Content(schema = @Schema(implementation = UserResponse.class), mediaType = "application/json")}
  )
  @ApiResponse(
      responseCode = "400",
      content = {@Content(schema = @Schema(implementation = ErrorMsgResponse.class), mediaType = "application/json")}
  )
  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(@PathVariable int id, @RequestBody @Valid UserUpsertRequest request) {
    final User editedUser = this.userMapper.requestToUser(request);
    final User updatedUser = this.userService.update(id, editedUser);
    final UserResponse response = this.userMapper.userToUserResponse(updatedUser);
    return ResponseEntity.ok(response);
  }

  @Operation(
      summary = "Удалить пользователя по номеру",
      description = "Удаляет пользователя по номеру",
      tags = {"Номер", "Удаление"}
  )
  @ApiResponse(
      responseCode = "204"
  )
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

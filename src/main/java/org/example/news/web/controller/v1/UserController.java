package org.example.news.web.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.UserMapper;
import org.example.news.service.UserService;
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
public class UserController {
  private final UserService userService;
  private final UserMapper userMapper;

  @GetMapping
  public ResponseEntity<UserListResponse> findAll() {
    final List<User> allUsers = this.userService.findAll();
    final UserListResponse response = this.userMapper.userListToUserListResponse(allUsers);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserResponse> findById(@PathVariable int id) {
    final User foundUser = this.userService.findById(id);
    final UserResponse response = this.userMapper.userToResponse(foundUser);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<UserResponse> create(@RequestBody @Valid UserUpsertRequest request) {
    final User newUser = this.userMapper.requestToUser(request);
    final User savedUser = this.userService.save(newUser);
    final UserResponse response = this.userMapper.userToResponse(savedUser);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserResponse> update(@PathVariable int id, @RequestBody @Valid UserUpsertRequest request) {
    final User editedUser = this.userMapper.requestToUser(request);
    final User updatedUser = this.userService.update(id, editedUser);
    final UserResponse response = this.userMapper.userToResponse(updatedUser);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.userService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

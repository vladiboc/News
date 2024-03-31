package org.example.news.service;

import org.example.news.db.entity.User;

import java.util.List;

public interface UserService {
  List<User> findAll();
  User findById(int id);
  User save(User user);
  User update(int id, User user);
  void deleteById(int id);
}

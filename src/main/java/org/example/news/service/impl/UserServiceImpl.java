package org.example.news.service.impl;

import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.service.UserService;
import org.example.news.service.core.UniversalServiceImpl;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends UniversalServiceImpl<User> implements UserService {
  public UserServiceImpl(UserRepository userRepository) {
    super(userRepository, ErrorMsg.USER_BY_ID_NOT_FOUND);
  }
}

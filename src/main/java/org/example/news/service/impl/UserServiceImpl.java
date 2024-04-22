package org.example.news.service.impl;

import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.service.UserService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.ErrorMsg;
import org.example.news.web.dto.news.NewsFilter;
import org.example.news.web.dto.user.UserFilter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends AbstractUniversalService<User, UserFilter> implements UserService {
  public UserServiceImpl(UserRepository userRepository) {
    super(userRepository, ErrorMsg.USER_BY_ID_NOT_FOUND);
  }

  @Override
  public List<User> findAllByFilter(UserFilter filter) {
    return null;
  }
}

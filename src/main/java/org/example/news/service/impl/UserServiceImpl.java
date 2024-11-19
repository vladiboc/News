package org.example.news.service.impl;

import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.service.UserService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.constant.ErrorMsg;
import org.example.news.web.dto.user.UserFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends AbstractUniversalService<User, UserFilter> implements UserService {
  public UserServiceImpl(UserRepository userRepository) {
    super(userRepository, ErrorMsg.USER_BY_ID_NOT_FOUND);
  }

  @Loggable
  @Override
  public List<User> findAllByFilter(UserFilter filter) {
    return super.repository.findAll(
        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
    ).getContent();
  }
}

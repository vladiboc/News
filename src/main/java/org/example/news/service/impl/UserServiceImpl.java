package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.service.UserService;
import org.example.news.util.BeanUtils;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;

  @Override
  public List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public User findById(int id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            MessageFormat.format(ErrorMsg.USER_BY_ID_NOT_FOUND, id))
        );
  }

  @Override
  public User save(User user) {
    return userRepository.save(user);
  }

  @Override
  public User update(int id, User user) {
    User existedUser = this.findById(id);
    BeanUtils.copyNonNullFields(user, existedUser);
    existedUser.setId(id);
    return userRepository.save(existedUser);
  }

  @Override
  public void deleteById(int id) {
    userRepository.deleteById(id);
  }
}

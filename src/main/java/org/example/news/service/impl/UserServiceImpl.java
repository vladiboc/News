package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.constant.ErrorMsg;
import org.example.news.db.entity.User;
import org.example.news.db.repository.UserRepository;
import org.example.news.service.UserService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.BeanUtils;
import org.example.news.web.dto.user.UserFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Имплементация сервиса пользователей.
 */
@Service
@Loggable
public class UserServiceImpl
    extends AbstractUniversalService<User, UserFilter> implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserServiceImpl(
      final UserRepository userRepository, final PasswordEncoder passwordEncoder
  ) {
    super(userRepository, ErrorMsg.USER_BY_ID_NOT_FOUND);
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public List<User> findAllByFilter(final UserFilter filter) {
    return super.repository.findAll(
        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
    ).getContent();
  }

  @Override
  public User createNewUser(final User newUser) {
    newUser.setPassword(this.passwordEncoder.encode(newUser.getPassword()));
    newUser.getRoles().stream().forEach(role -> role.setUser(newUser));
    return super.save(newUser);
  }

  @Override
  public User update(final int id, final User editedUser) {
    final var existedUser = super.findById(id);
    editedUser.setPassword(this.passwordEncoder.encode(editedUser.getPassword()));
    BeanUtils.copyNonNullFields(editedUser, existedUser);
    existedUser.setId(id);
    existedUser.getRoles().stream().forEach(role -> role.setUser(existedUser));
    return super.save(existedUser);
  }

  @Override
  public User findByName(final String userName) {
    return this.userRepository.findByName(userName).orElseThrow(() -> new EntityNotFoundException(
        MessageFormat.format(ErrorMsg.USER_BY_NAME_NOT_FOUND, userName)
    ));
  }
}

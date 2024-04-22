package org.example.news.service;

import org.example.news.db.entity.User;
import org.example.news.service.core.UniversalService;
import org.example.news.web.dto.user.UserFilter;

public interface UserService extends UniversalService<User, UserFilter> {
}

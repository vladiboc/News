package org.example.news.security;

import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Role;
import org.example.news.db.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class AppUserPrincipal implements UserDetails {
  private final User user;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return this.user.getRoles().stream().map(Role::toAuthority).toList();
  }

  @Override
  public String getPassword() {
    return this.user.getPassword();
  }

  @Override
  public String getUsername() {
    return this.user.getName();
  }

  public int getUserId() {
    return this.user.getId();
  }
}

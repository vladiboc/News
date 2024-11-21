package org.example.news.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(12);
  }

  @Bean
  AuthenticationManager dbAuthenticationManager(
      final HttpSecurity http,
      final UserDetailsService userDetailsService,
      final PasswordEncoder passwordEncoder
  ) throws Exception {
    final var authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

    authManagerBuilder.userDetailsService(userDetailsService);

    final var authProvider = new DaoAuthenticationProvider(passwordEncoder);
    authProvider.setUserDetailsService(userDetailsService);

    authManagerBuilder.authenticationProvider(authProvider);

    return authManagerBuilder.build();
  }
}

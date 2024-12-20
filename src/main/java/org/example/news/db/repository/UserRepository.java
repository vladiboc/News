package org.example.news.db.repository;

import java.util.Optional;
import org.example.news.db.entity.User;
import org.example.news.service.core.AbstractUniversalService;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository extends
    JpaRepository<User, Integer>,
    JpaSpecificationExecutor<User>,
    AbstractUniversalService.UniversalRepository<User> {

  @EntityGraph(attributePaths = {"roles"})
  Optional<User> findByName(String userName);
}

package org.example.news.db.repository;

import org.example.news.db.entity.Comment;
import org.example.news.service.core.AbstractUniversalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CommentRepository extends
    JpaRepository<Comment, Integer>,
    JpaSpecificationExecutor<Comment>,
    AbstractUniversalService.UniversalRepository<Comment> {
}

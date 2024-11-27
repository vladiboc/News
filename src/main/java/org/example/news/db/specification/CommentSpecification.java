package org.example.news.db.specification;

import jakarta.annotation.Nullable;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.User;
import org.example.news.web.dto.comment.CommentFilter;
import org.springframework.data.jpa.domain.Specification;

/**
 * Спецификация для поиска в БД комментариев, созданных заданным пользователем.
 */
public interface CommentSpecification {
  static Specification<Comment> withFilter(CommentFilter filter) {
    return Specification.where(CommentSpecification.byUserId(filter.getUserId()));
  }

  @Nullable
  static Specification<Comment> byUserId(Integer userId) {
    return (root, query, criteriaBuilder) -> {
      if (userId == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get(Comment.Fields.user).get(User.Fields.id), userId);
    };
  }
}

/**
 * Спецификация для поиска новостей по заданному фильтру
 */
package org.example.news.db.specification;

import jakarta.annotation.Nullable;
import org.example.news.db.entity.Category;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.web.dto.news.NewsFilter;
import org.springframework.data.jpa.domain.Specification;

public interface NewsSpecification {

  static Specification<News> withFilter(NewsFilter filter) {
    return Specification.where(NewsSpecification.byUserId(filter.getUserId()))
        .and(NewsSpecification.byUserName(filter.getUserName()))
        .and(NewsSpecification.byCategoryId(filter.getCategoryId()))
        .and(NewsSpecification.byCategoryName(filter.getCategoryName()));
  }

  @Nullable
  static Specification<News> byUserId(Integer userId) {
    return (root, query, criteriaBuilder) -> {
      if (userId == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get(News.Fields.user).get(User.Fields.id), userId);
    };
  }

  @Nullable
  static Specification<News> byUserName(String userName) {
    return (root, query, criteriaBuilder) -> {
      if (userName == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get(News.Fields.user).get(User.Fields.name), userName);
    };
  }

  @Nullable
  static Specification<News> byCategoryId(Integer categoryId) {
    return (root, query, criteriaBuilder) -> {
      if (categoryId == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get(News.Fields.category).get(Category.Fields.id), categoryId);
    };
  }

  @Nullable
  static Specification<News> byCategoryName(String categoryName) {
    return (root, query, criteriaBuilder) -> {
      if (categoryName == null) {
        return null;
      }
      return criteriaBuilder.equal(root.get(News.Fields.category).get(Category.Fields.name), categoryName);
    };
  }
}

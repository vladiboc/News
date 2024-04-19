package org.example.news.mapper.v1;

import org.example.news.db.entity.Category;
import org.example.news.db.entity.News;
import org.example.news.service.CategoryService;
import org.example.news.service.UserService;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class NewsMapperDelegate implements NewsMapper {
  @Autowired
  private UserService userService;
  @Autowired
  private CategoryService categoryService;

  @Override
  public News requestToNews(NewsUpsertRequest request) {
    News news = new News(request.getTitle(), request.getContent());

    news.setUser(this.userService.findById(request.getUserId()));
    List<Category> newCategories = request.getCategoryIds().stream()
        .map(this.categoryService::findById)
        .toList();
    news.setCategories(new ArrayList<>(newCategories));

    return news;
  }
}

package org.example.news.mapper.v1;


import org.example.news.db.entity.Category;
import org.example.news.db.entity.News;
import org.example.news.web.dto.category.CategoryListResponse;
import org.example.news.web.dto.category.CategoryResponse;
import org.example.news.web.dto.category.CategoryResponseForList;
import org.example.news.web.dto.category.CategoryUpsertRequest;
import org.example.news.web.dto.news.NewsResponseForList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryMapper {
  private NewsMapper newsMapper;

  @Autowired
  public void setNewsMapper(NewsMapper newsMapper) {
    this.newsMapper = newsMapper;
  }

  public Category requestToCategory(CategoryUpsertRequest request) {
    return new Category(request.getName());
  }

  public CategoryResponse categoryToCategoryResponse(Category category) {
    CategoryResponse response = new CategoryResponse(category.getId(), category.getName());

    List<News> news = category.getNews();
    List<NewsResponseForList> newsResponses = this.newsMapper.newsListToListOfNewsResponse(news);
    response.setNews(newsResponses);

    return response;
  }

  public CategoryListResponse categoryListToCategoryListResponse(List<Category> categories) {
    List<CategoryResponseForList> categoryResponses = this.categoryListToListOfCategoryResponse(categories);
    return new CategoryListResponse(categoryResponses);
  }

  public List<CategoryResponseForList> categoryListToListOfCategoryResponse(List<Category> categories) {
    return categories.stream()
        .map(this::categoryToCategoryForList)
        .toList();
  }

  private CategoryResponseForList categoryToCategoryForList(Category category) {
    return new CategoryResponseForList(category.getId(), category.getName(), category.getNews().size());
  }
}

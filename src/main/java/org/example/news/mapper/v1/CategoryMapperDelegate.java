package org.example.news.mapper.v1;

import org.example.news.db.entity.Category;
import org.example.news.db.entity.News;
import org.example.news.web.dto.category.CategoryResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class CategoryMapperDelegate implements CategoryMapper {
  private NewsMapper newsMapper;

  @Autowired
  public void setNewsMapper(NewsMapper newsMapper) {
    this.newsMapper = newsMapper;
  }
  @Override
  public CategoryResponse categoryToCategoryResponse(Category category) {
    CategoryResponse response = new CategoryResponse(category.getId(), category.getName());

    List<News> news = category.getNews();
    List<NewsResponseForList> newsResponses = this.newsMapper.newsListToListOfNewsResponse(news);
    response.setNews(newsResponses);

    return response;
  }
}

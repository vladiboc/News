package org.example.news.mapper.v1;

import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Category;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.service.CategoryService;
import org.example.news.service.UserService;
import org.example.news.web.dto.category.CategoryResponseForList;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NewsMapper {
  private final UserService userService;
  private final CategoryService categoryService;
  private final CommentMapper commentMapper;
  private CategoryMapper categoryMapper;

  @Autowired
  public void setCategoryMapper(CategoryMapper categoryMapper) {
    this.categoryMapper = categoryMapper;
  }

  public News requestToNews(NewsUpsertRequest request) {
    News news = new News(request.getTitle(), request.getContent());

    news.setUser(this.userService.findById(request.getUserId()));
    news.setCategories(request.getCategoryIds().stream()
        .map(this.categoryService::findById)
        .toList());

    return news;
  }

  public NewsResponse newsToNewsResponse(News news) {
    NewsResponse newsResponse = new NewsResponse(
        news.getId(),
        news.getTitle(),
        news.getContent(),
        news.getUser().getId()
    );

    List<Category> categories = news.getCategories();
    List<CategoryResponseForList> categoryResponses = this.categoryMapper.categoryListToListOfCategoryResponse(categories);
    newsResponse.setCategories(categoryResponses);

    List<Comment> comments = news.getComments();
    List<CommentResponse> commentResponses = this.commentMapper.commentListToListOfCommentResponse(comments);
    newsResponse.setComments(commentResponses);

    return newsResponse;
  }

  public NewsListResponse newsListToNewsListResponse(List<News> news) {
    List<NewsResponseForList> newsResponses = this.newsListToListOfNewsResponse(news);
    return new NewsListResponse(newsResponses);
  }

  public List<NewsResponseForList> newsListToListOfNewsResponse(List<News> news) {
    return news.stream()
        .map(this::newsToNewsResponseForList)
        .toList();
  }

  private NewsResponseForList newsToNewsResponseForList(News news) {
    return new NewsResponseForList(
        news.getId(),
        news.getTitle(),
        news.getContent(),
        news.getUser().getId(),
        news.getCategories().size(),
        news.getComments().size()
    );
  }
}

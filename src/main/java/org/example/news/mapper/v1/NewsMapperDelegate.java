package org.example.news.mapper.v1;

import org.example.news.db.entity.Category;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.service.CategoryService;
import org.example.news.service.UserService;
import org.example.news.web.dto.category.CategoryResponseForList;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class NewsMapperDelegate implements NewsMapper {
  @Autowired
  private UserService userService;
  @Autowired
  private CategoryService categoryService;
  @Autowired
  private CommentMapper commentMapper;
  private CategoryMapper categoryMapper;

  @Autowired
  public void setCategoryMapper(CategoryMapper categoryMapper) {
    this.categoryMapper = categoryMapper;
  }
  @Override
  public News requestToNews(NewsUpsertRequest request) {
    News news = new News(request.getTitle(), request.getContent());

    news.setUser(this.userService.findById(request.getUserId()));
    news.setCategories(request.getCategoryIds().stream()
        .map(this.categoryService::findById)
        .toList());

    return news;
  }
  @Override
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
}

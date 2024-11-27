package org.example.news.mapper.v1;

import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.News;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {CategoryMapper.class, CommentMapper.class})
public interface NewsMapper {
  News requestToNews(NewsUpsertRequest request);

  @Mapping(source = "news.user.id", target = "userId")
  @Mapping(source = "news.category.id", target = "categoryId")
  NewsResponse newsToNewsResponse(News news);

  @Loggable
  default NewsResponseForList newsToNewsResponseForList(News news) {
    return new NewsResponseForList(
        news.getId(),
        news.getTitle(),
        news.getContent(),
        news.getUser().getId(),
        news.getCategory().getId(),
        news.getComments().size()
    );
  }

  List<NewsResponseForList> newsListToListOfNewsResponse(List<News> news);

  @Loggable
  default NewsListResponse newsListToNewsListResponse(List<News> news) {
    return new NewsListResponse(this.newsListToListOfNewsResponse(news));
  }
}

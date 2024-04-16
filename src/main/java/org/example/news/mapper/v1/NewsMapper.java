package org.example.news.mapper.v1;

import org.example.news.db.entity.News;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsResponseForList;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CategoryMapper.class, CommentMapper.class})
public interface NewsMapper {
  News requestToNews(NewsUpsertRequest request);
  @Mapping(source = "news.user.id", target = "userId")
  NewsResponse newsToNewsResponse(News news);
  default NewsResponseForList newsToNewsResponseForList(News news) {
    return new NewsResponseForList(
        news.getId(),
        news.getTitle(),
        news.getContent(),
        news.getUser().getId(),
        news.getCategories().size(),
        news.getComments().size()
    );
  }
  List<NewsResponseForList> newsListToListOfNewsResponse(List<News> news);
  default NewsListResponse newsListToNewsListResponse(List<News> news) {
    return new NewsListResponse(this.newsListToListOfNewsResponse(news));
  }
}

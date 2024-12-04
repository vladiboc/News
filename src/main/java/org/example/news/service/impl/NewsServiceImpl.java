package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.constant.ErrorMsg;
import org.example.news.db.entity.News;
import org.example.news.db.repository.NewsRepository;
import org.example.news.db.specification.NewsSpecification;
import org.example.news.service.NewsService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.web.dto.news.NewsFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Loggable
public class NewsServiceImpl
    extends AbstractUniversalService<News, NewsFilter> implements NewsService {
  public NewsServiceImpl(NewsRepository newsRepository) {
    super(newsRepository, ErrorMsg.NEWS_BY_ID_NOT_FOUND);
  }

  @Override
  public List<News> findAllByFilter(NewsFilter filter) {
    return super.repository.findAll(NewsSpecification.withFilter(filter),
        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
    ).getContent();
  }
}

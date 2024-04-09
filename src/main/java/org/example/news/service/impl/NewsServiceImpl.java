package org.example.news.service.impl;

import org.example.news.db.entity.News;
import org.example.news.db.repository.NewsRepository;
import org.example.news.service.NewsService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

@Service
public class NewsServiceImpl extends AbstractUniversalService<News> implements NewsService {
  public NewsServiceImpl(NewsRepository newsRepository) {
    super(newsRepository, ErrorMsg.NEWS_BY_ID_NOT_FOUND);
  }
}

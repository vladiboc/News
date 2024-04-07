package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.News;
import org.example.news.db.repository.NewsRepository;
import org.example.news.service.NewsService;
import org.example.news.service.core.UniversalServiceImpl;
import org.example.news.util.BeanUtils;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class NewsServiceImpl extends UniversalServiceImpl<News> implements NewsService {
  public NewsServiceImpl(NewsRepository newsRepository) {
    super(newsRepository, ErrorMsg.NEWS_BY_ID_NOT_FOUND);
  }
}

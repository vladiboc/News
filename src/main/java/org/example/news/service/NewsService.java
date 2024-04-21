package org.example.news.service;

import org.example.news.db.entity.News;
import org.example.news.service.core.UniversalService;
import org.example.news.web.dto.news.NewsFilter;

import java.util.List;

public interface NewsService extends UniversalService<News> {
  List<News> findAllByFilter(NewsFilter filter);
}

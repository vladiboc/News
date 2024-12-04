package org.example.news.service;

import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.service.core.UniversalService;
import org.example.news.web.dto.news.NewsFilter;

public interface NewsService extends UniversalService<News, NewsFilter> {
}

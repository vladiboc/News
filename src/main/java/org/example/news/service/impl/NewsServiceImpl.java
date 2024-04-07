package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.News;
import org.example.news.db.repository.NewsRepository;
import org.example.news.service.NewsService;
import org.example.news.util.BeanUtils;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {
  private final NewsRepository newsRepository;
  @Override
  public List<News> findAll() {
    return this.newsRepository.findAll();
  }

  @Override
  public News findById(int id) {
    return this.newsRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            MessageFormat.format(ErrorMsg.NEWS_BY_ID_NOT_FOUND, id)
          )
        );
  }

  @Override
  public News save(News news) {
    return this.newsRepository.save(news);
  }

  @Override
  public News update(int id, News news) {
    final News existedNews = this.findById(news.getId());
    BeanUtils.copyNonNullFields(news, existedNews);
    return this.newsRepository.save(existedNews);
  }

  @Override
  public void deleteById(int id) {
    this.newsRepository.deleteById(id);
  }
}

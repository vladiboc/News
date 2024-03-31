//package org.example.news.service.impl;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.example.news.db.entity.News;
//import org.example.news.db.repository.NewsRepository;
//import org.example.news.service.NewsService;
//import org.example.news.util.BeanUtils;
//import org.springframework.stereotype.Service;
//
//import java.text.MessageFormat;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class NewsServiceImpl implements NewsService {
//  private final NewsRepository newsRepository;
//  @Override
//  public List<News> findAll() {
//    return newsRepository.findAll();
//  }
//
//  // TODO заменить и обратотать исключение
//  @Override
//  public News findById(int id) {
//    return newsRepository.findById(id)
//        .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
//            "Пользователь с id {0} не найден!", id
//        )));
//  }
//
//  @Override
//  public News save(News news) {
//    return newsRepository.save(news);
//  }
//
//  @Override
//  public News update(int id, News news) {
//    News existedNews = this.findById(news.getId());
//    BeanUtils.copyNonNullFields(news, existedNews);
//    return newsRepository.save(existedNews);
//  }
//
//  @Override
//  public void deleteById(int id) {
//
//  }
//}

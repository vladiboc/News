package org.example.news.web.controller.v1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.News;
import org.example.news.mapper.v1.NewsMapper;
import org.example.news.service.NewsService;
import org.example.news.web.dto.news.NewsListResponse;
import org.example.news.web.dto.news.NewsResponse;
import org.example.news.web.dto.news.NewsUpsertRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/news")
@RequiredArgsConstructor
public class NewsController {
  private final NewsService newsService;
  private final NewsMapper newsMapper;

  @GetMapping
  public ResponseEntity<NewsListResponse> findAll() {
    final List<News> news = this.newsService.findAll();
    final NewsListResponse response = this.newsMapper.newsListToNewsListResponse(news);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<NewsResponse> findById(@PathVariable int id) {
    final News news = this.newsService.findById(id);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(news);
    return ResponseEntity.ok(response);
  }

  @PostMapping
    public ResponseEntity<NewsResponse> create(@RequestBody @Valid NewsUpsertRequest request) {
    final News newNews = this.newsMapper.requestToNews(request);
    final News createdNews = this.newsService.save(newNews);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(createdNews);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<NewsResponse> update(@PathVariable int id, @RequestBody @Valid NewsUpsertRequest request) {
    final News editedNews = this.newsMapper.requestToNews(request);
    final News updatedNews = this.newsService.update(id, editedNews);
    final NewsResponse response = this.newsMapper.newsToNewsResponse(updatedNews);
    return ResponseEntity.ok(response);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable int id) {
    this.newsService.deleteById(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}

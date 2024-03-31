package org.example.news.web.dto.news;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsListResponse {
  private List<NewsForList> newsList = new ArrayList<>();
}

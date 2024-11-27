package org.example.news.web.dto.news;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsListResponse {
  private List<NewsResponseForList> newsList = new ArrayList<>();
}

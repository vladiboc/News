package org.example.news.web.dto.category;

import lombok.Data;
import org.example.news.web.dto.news.NewsForList;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryResponse {
  private int id;
  private String name;
  private List<NewsForList> news = new ArrayList<>();
}

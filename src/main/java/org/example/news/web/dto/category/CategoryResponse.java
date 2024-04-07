package org.example.news.web.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.web.dto.news.NewsResponseForList;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class CategoryResponse {
  private int id;
  private String name;
  private List<NewsResponseForList> news = new ArrayList<>();

  public CategoryResponse(int id, String name) {
    this.id = id;
    this.name = name;
  }
}

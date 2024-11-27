package org.example.news.web.dto.category;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.web.dto.news.NewsResponseForList;

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

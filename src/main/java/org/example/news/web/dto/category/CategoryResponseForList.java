package org.example.news.web.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class CategoryResponseForList {
  private int id;
  private String name;
  private int newsCount;
}

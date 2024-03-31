package org.example.news.web.dto.category;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategoryListResponse {
  private List<CategoryForList> categories = new ArrayList<>();
}

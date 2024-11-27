package org.example.news.web.dto.category;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryListResponse {
  private List<CategoryResponseForList> categories = new ArrayList<>();
}

package org.example.news.web.dto.news;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsUpsertRequest {
  private String title;
  private String content;
  private List<String> categories = new ArrayList<>();
}

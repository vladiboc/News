package org.example.news.web.dto.news;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.web.dto.category.CategoryResponseForList;
import org.example.news.web.dto.comment.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class NewsResponse {
  private int id;
  private String title;
  private String content;
  private int userId;
  private List<CategoryResponseForList> categories = new ArrayList<>();
  private List<CommentResponse> comments = new ArrayList<>();

  public NewsResponse(int id, String title, String content, int userId) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.userId = userId;
  }
}

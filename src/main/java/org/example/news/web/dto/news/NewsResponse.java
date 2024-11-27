package org.example.news.web.dto.news;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.web.dto.comment.CommentResponse;

@Data
@NoArgsConstructor
public class NewsResponse {
  private int id;
  private String title;
  private String content;
  private int userId;
  private int categoryId;
  private List<CommentResponse> comments = new ArrayList<>();

  public NewsResponse(int id, String title, String content, int userId, int categoryId) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.userId = userId;
    this.categoryId = categoryId;
  }
}

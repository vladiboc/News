package org.example.news.web.dto.user;

import jakarta.annotation.Nullable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsResponseForList;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UserResponse {
  private int id;
  private String name;
  private List<NewsResponseForList> news = new ArrayList<>();
  private List<CommentResponse> comments = new ArrayList<>();

  public UserResponse(int id, String name) {
    this.id = id;
    this.name = name;
  }
}

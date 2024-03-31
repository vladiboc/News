package org.example.news.web.dto.news;

import lombok.Data;
import org.example.news.db.entity.User;
import org.example.news.web.dto.category.CategoryForList;
import org.example.news.web.dto.comment.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Data
public class NewsResponse {
  private int id;
  private String title;
  private String content;
  private User user;
  private List<CategoryForList> categories = new ArrayList<>();
  private List<CommentResponse> comments = new ArrayList<>();
}

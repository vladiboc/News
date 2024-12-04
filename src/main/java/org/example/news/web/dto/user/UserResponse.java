package org.example.news.web.dto.user;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.db.entity.RoleType;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.news.NewsResponseForList;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
  private int id;
  private String name;
  private List<RoleType> roles = new ArrayList<>();
  private List<NewsResponseForList> news = new ArrayList<>();
  private List<CommentResponse> comments = new ArrayList<>();
}
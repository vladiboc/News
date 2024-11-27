package org.example.news.web.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseForList {
  private int id;
  private String name;
  private int newsCount;
  private int commentsCount;
}

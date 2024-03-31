package org.example.news.web.dto.user;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
  private List<UserResponseForList> users = new ArrayList<>();
}

package org.example.news.web.dto.user;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserListResponse {
  private List<UserResponseForList> users = new ArrayList<>();
}

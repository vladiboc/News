package org.example.news.web.dto.comment;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentListResponse {
  private List<CommentResponse> comments = new ArrayList<>();
}

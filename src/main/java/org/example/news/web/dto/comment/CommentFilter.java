package org.example.news.web.dto.comment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.constant.ErrorMsg;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentFilter {
  @NotNull(message = ErrorMsg.COMMENT_USER_ID_MUST_BE_FILLED)
  @PositiveOrZero(message = ErrorMsg.COMMENT_USER_ID_MUST_BE_FILLED)
  private Integer userId;
}

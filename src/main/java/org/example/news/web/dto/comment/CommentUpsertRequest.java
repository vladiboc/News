package org.example.news.web.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.constant.ErrorMsg;
import org.example.news.constant.StringSizes;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpsertRequest {
  @NotBlank(message = ErrorMsg.COMMENT_CONTENT_MUST_BE_FILLED)
  @Size(
      min = StringSizes.COMMENT_CONTENT_MIN,
      max = StringSizes.COMMENT_CONTENT_MAX,
      message = ErrorMsg.COMMENT_SIZE_FROM_MIN_TO_MAX
  )
  private String content;
  @Positive(message = ErrorMsg.COMMENT_NEWS_ID_MUST_BE_POSITIVE)
  private int newsId;
  @Positive(message = ErrorMsg.COMMENT_USER_ID_MUST_BE_POSITIVE)
  private int userId;
}


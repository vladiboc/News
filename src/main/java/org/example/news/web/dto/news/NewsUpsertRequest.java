package org.example.news.web.dto.news;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.util.ErrorMsg;
import org.example.news.util.StringSizes;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewsUpsertRequest {
  @NotBlank(message = ErrorMsg.NEWS_TITLE_MUST_BE_FILLED)
  @Size(
      min = StringSizes.NEWS_TITLE_MIN,
      max = StringSizes.NEWS_TITLE_MAX,
      message = ErrorMsg.NEWS_TITLE_SIZE_FROM_MIN_TO_MAX
  )
  private String title;
  @NotBlank(message = ErrorMsg.NEWS_CONTENT_MUST_BE_FILLED)
  @Size(
      min = StringSizes.NEWS_CONTENT_MIN,
      max = StringSizes.NEWS_CONTENT_MAX,
      message = ErrorMsg.NEWS_CONTENT_SIZE_FROM_MIN_TO_MAX
  )
  private String content;
  @Positive(message = ErrorMsg.NEWS_USER_ID_MUST_BE_POSITIVE)
  private int userId;
  @NotNull(message = ErrorMsg.NEWS_CATEGORIES_MUST_BE_FILLED)
  private List<Integer> categoryIds = new ArrayList<>();

  public NewsUpsertRequest(String title, String content, int userId) {
    this.title = title;
    this.content = content;
    this.userId = userId;
  }
}

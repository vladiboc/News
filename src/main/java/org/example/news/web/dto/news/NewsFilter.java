package org.example.news.web.dto.news;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.util.ErrorMsg;

@Data
@NoArgsConstructor
public class NewsFilter {
  @NotNull(message = ErrorMsg.PAGE_SIZE_MUST_BE_FILLED)
  @PositiveOrZero(message = ErrorMsg.PAGE_SIZE_MUST_BE_FILLED)
  private Integer pageSize;
  @NotNull(message = ErrorMsg.PAGE_NUMBER_MUST_BE_FILLED)
  @PositiveOrZero(message = ErrorMsg.PAGE_NUMBER_MUST_BE_FILLED)
  private Integer pageNumber;
  private Integer userId;
  private String userName;
  private Integer categoryId;
  private String categoryName;

  public NewsFilter(Integer pageSize, Integer pageNumber) {
    this.pageSize = pageSize;
    this.pageNumber = pageNumber;
  }
}

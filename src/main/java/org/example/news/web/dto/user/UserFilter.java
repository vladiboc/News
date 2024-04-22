package org.example.news.web.dto.user;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.example.news.util.ErrorMsg;

@Data
public class UserFilter {
  @NotNull(message = ErrorMsg.PAGE_SIZE_MUST_BE_FILLED)
  @PositiveOrZero(message = ErrorMsg.PAGE_SIZE_MUST_BE_FILLED)
  private Integer pageSize;
  @NotNull(message = ErrorMsg.PAGE_NUMBER_MUST_BE_FILLED)
  @PositiveOrZero(message = ErrorMsg.PAGE_NUMBER_MUST_BE_FILLED)
  private Integer pageNumber;
}

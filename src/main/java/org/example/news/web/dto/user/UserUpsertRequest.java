package org.example.news.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.example.news.constant.ErrorMsg;
import org.example.news.constant.StringSizes;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpsertRequest {
    @NotBlank(message = ErrorMsg.USER_NAME_MUST_BE_FILLED)
    @Size(
        min = StringSizes.USER_NAME_MIN,
        max = StringSizes.USER_NAME_MAX,
        message = ErrorMsg.USER_NAME_SIZE_FROM_MIN_TO_MAX
    )
    private String name;
}

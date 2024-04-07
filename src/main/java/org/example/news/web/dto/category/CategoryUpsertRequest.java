package org.example.news.web.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.util.ErrorMsg;
import org.example.news.util.StringSizes;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryUpsertRequest {
    @NotBlank(message = ErrorMsg.CATEGORY_NAME_MUST_BE_FILLED)
    @Size(
        min = StringSizes.CATEGORY_NAME_MIN,
        max = StringSizes.CATEGORY_NAME_MAX,
        message = ErrorMsg.CATEGORY_NAME_SIZE_FROM_MIN_TO_MAX
    )
    private String name;
}

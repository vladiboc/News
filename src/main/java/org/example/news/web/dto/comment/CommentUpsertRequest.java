package org.example.news.web.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.util.ErrorMsg;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpsertRequest {
    @NotBlank(message = ErrorMsg.COMMENT_CONTENT_MUST_BE_FILLED)
    @Size(min = 2, max = 512, message = ErrorMsg.COMMENT_SIZE_FROM_MIN_TO_MAX)
    private String content;

    @Positive(message = ErrorMsg.COMMENT_USER_ID_MUST_BE_POSITIVE)
    private int userId;

    public CommentUpsertRequest(int userId) {
        this.userId = userId;
    }

    public CommentUpsertRequest(String content) {
        this.content = content;
    }
}


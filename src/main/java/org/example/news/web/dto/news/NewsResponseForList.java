package org.example.news.web.dto.news;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewsResponseForList {
    private int id;
    private String title;
    private String content;
    private int userId;
    private int categoriesCount;
    private int commentsCount;
}

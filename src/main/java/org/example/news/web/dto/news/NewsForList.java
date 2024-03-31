package org.example.news.web.dto.news;

import lombok.Data;

@Data
public class NewsForList {
    private int id;
    private String title;
    private String content;
    private int userId;
    private int categoriesCount;
    private int commentsCount;
}

package org.example.news.web.dto.category;

import lombok.Data;

@Data
public class CategoryForList {
    private int id;
    private String name;
    private int newsCount;
}

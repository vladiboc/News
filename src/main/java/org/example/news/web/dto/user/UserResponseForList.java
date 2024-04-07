package org.example.news.web.dto.user;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseForList {
    private int id;
    private String name;
    private int newsCount;
    private int commentsCount;
}

package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@Entity(name = "comments")
public class Comment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String content;
  @ManyToOne
  @JoinColumn(name = "news_id")
  @ToString.Exclude
  private News news;
  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant updatedAt;

  public Comment(String content) {
    this.content = content;
  }

  public Comment(String content, News news, User user) {
    this(content);
    this.news = news;
    this.user = user;
  }
  public Comment(int id, String content, News news, User user) {
    this(content, news, user);
    this.id = id;
  }
}

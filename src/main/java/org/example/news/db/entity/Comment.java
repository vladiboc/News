package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.example.news.db.entity.core.Identifiable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@NoArgsConstructor
@FieldNameConstants
@Entity(name = "comments")
public class Comment implements Identifiable {
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

  public Comment(String content, User user) {
    this(content);
    this.user = user;
  }

  public Comment(String content, News news, User user) {
    this(content, user);
    this.news = news;
  }

  public Comment(int id, String content, News news, User user) {
    this(content, news, user);
    this.id = id;
  }
}

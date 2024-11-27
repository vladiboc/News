package org.example.news.db.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.Instant;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

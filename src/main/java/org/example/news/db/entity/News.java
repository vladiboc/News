package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldNameConstants
@Entity(name = "news")
public class News implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String title;
  private String content;
  @ManyToOne
  @JoinColumn(name = "user_id")
  @ToString.Exclude
  private User user;
  @ManyToOne
  @JoinColumn(name = "category_id")
  @ToString.Exclude
  private Category category;
  @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();
  @CreationTimestamp
  @Column(name = "created")
  private Instant createdAt;
  @UpdateTimestamp
  @Column(name = "updated")
  private Instant updatedAt;

  public News(String title, String content) {
    this.title = title;
    this.content = content;
  }

  public News(String title, String content, User user) {
    this(title, content);
    this.user = user;
  }

  public News(String title, String content, User user, Category category) {
    this(title, content, user);
    this.category = category;
  }

  public News(int id, String title, String content, User user, Category category) {
    this(title, content, user, category);
    this.id = id;
  }
}

package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.news.db.entity.core.Identifiable;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
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
  @ManyToMany
  @JoinTable(name = "news_categories",
      joinColumns = {
          @JoinColumn(name = "news_id")
      },
      inverseJoinColumns = {
          @JoinColumn(name = "category_id")
      }
  )
  @ToString.Exclude
  private List<Category> categories = new ArrayList<>();
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

  public News(int id, String title, String content, User user) {
    this(title, content, user);
    this.id = id;
  }
}

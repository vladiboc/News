package org.example.news.db.entity;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "users")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "user_name")
  private String name;
//  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//  @ToString.Exclude
//  private List<News> news = new ArrayList<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<Comment> comments = new ArrayList<>();
  @CreationTimestamp
  private Instant createdAt;
  @UpdateTimestamp
  private Instant updatedAt;

  public User(String name) {
    this.name = name;
  }

  public User(int id, String name) {
    this(name);
    this.id = id;
  }

  public User(int id, String name, @Nullable Comment comment) {
    this(id, name);
    if (comment != null) {
      comment.setUser(this);
      this.getComments().add(comment);
    }
  }
}

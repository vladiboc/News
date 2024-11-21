package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@FieldNameConstants
@Entity(name = "users")
public class User implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  @Column(name = "user_name", unique = true)
  private String name;
  @Column(name = "user_password")
  private String password;
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  private List<Role> roles = new ArrayList<>();
  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<News> news = new ArrayList<>();
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
}

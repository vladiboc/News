package org.example.news.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  public User(final String name) {
    this.name = name;
  }

  public User(final String name, final String password, final List<Role> roles) {
    this(name);
    this.password = password;
    this.roles = roles;
  }

  public User(final int id, final String name) {
    this(name);
    this.id = id;
  }
}

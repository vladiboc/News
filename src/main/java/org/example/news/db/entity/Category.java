package org.example.news.db.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Data
@NoArgsConstructor
@FieldNameConstants
@Entity(name = "categories")
public class Category implements Identifiable {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
  @ToString.Exclude
  private List<News> news = new ArrayList<>();
  @CreationTimestamp
  @Column(name = "created")
  private Instant createdAt;
  @UpdateTimestamp
  @Column(name = "updated")
  private Instant updatedAt;

  public Category(String name) {
    this.name = name;
  }

  public Category(int id, String name) {
    this(name);
    this.id = id;
  }
}

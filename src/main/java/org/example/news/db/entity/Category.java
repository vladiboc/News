package org.example.news.db.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "categories")
public class Category {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "news_categories",
      joinColumns = {
        @JoinColumn(name = "category_id")
      },
      inverseJoinColumns = {
        @JoinColumn(name = "news_id")
      }
  )
  private List<News> news = new ArrayList<>();

  public Category(String name) {
    this.name = name;
  }

  public Category(int id, String name) {
    this(name);
    this.id = id;
  }
}

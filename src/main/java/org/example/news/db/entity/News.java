//package org.example.news.db.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.UpdateTimestamp;
//
//import java.time.Instant;
//import java.util.ArrayList;
//import java.util.List;
//
//@Data
//@Entity(name = "news")
//public class News {
//  @Id
//  @GeneratedValue(strategy = GenerationType.IDENTITY)
//  private int id;
//  private String title;
//  private String content;
//  @ManyToOne
//  @JoinColumn(name = "user_id")
//  @ToString.Exclude
//  private User user;
//  @ManyToMany(cascade = CascadeType.ALL)
//  @JoinTable(name = "news_categories",
//      joinColumns = {
//          @JoinColumn(name = "news_id")
//      },
//      inverseJoinColumns = {
//          @JoinColumn(name = "category_id")
//      }
//  )
//  private List<Category> categories = new ArrayList<>();
//  @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
//  private List<Comment> comments = new ArrayList<>();
//  @CreationTimestamp
//  @Column(name = "created")
//  private Instant createdAt;
//  @UpdateTimestamp
//  @Column(name = "updated")
//  private Instant updatedAt;
//
//}

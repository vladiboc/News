package org.example.news.db.entity.core;

import java.time.Instant;

public interface Identifiable {
  int getId();
  void setId(int id);
  Instant getCreatedAt();
  void setCreatedAt(Instant updatedAt);
}

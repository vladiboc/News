package org.example.news.db.entity;

/**
 * Интерфейс для использования в классе AbstractUniversalService.
 */
public interface Identifiable {
  int getId();

  void setId(int id);
}

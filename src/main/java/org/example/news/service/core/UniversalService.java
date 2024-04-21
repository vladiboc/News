package org.example.news.service.core;

import org.example.news.db.repository.NewsSpecification;
import org.hibernate.mapping.Filterable;

import java.util.List;

public interface UniversalService<T> {
  List<T> findAll();
  T findById(int id);
  T save(T object);
  T update(int id, T object);
  void deleteById(int id);
}

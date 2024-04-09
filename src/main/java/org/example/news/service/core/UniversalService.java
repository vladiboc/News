package org.example.news.service.core;

import java.util.List;

public interface UniversalService<T> {
  List<T> findAll();
  T findById(int id);
  T save(T object);
  T update(int id, T object);
  void deleteById(int id);
}
package org.example.news.service.core;

import java.util.List;

public interface UniversalService<T, F> {
  List<T> findAllByFilter(F filter);
  T findById(int id);
  T save(T object);
  T update(int id, T object);
  void deleteById(int id);
}

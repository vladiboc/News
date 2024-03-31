package org.example.news.service.core;

import java.util.List;

public interface CommonService<T> {
  List<T> findAll();
  T findById(int id);
  T save(T object);
  T update(T object);
  void deleteById(int id);

}

package org.example.news.service;

import org.example.news.db.entity.Comment;

import java.util.List;

public interface CommentService {
  List<Comment> findAll();
  Comment findById(int id);
  Comment save(Comment comment);
  Comment update(int id, Comment comment);
  void deleteById(int id);

}

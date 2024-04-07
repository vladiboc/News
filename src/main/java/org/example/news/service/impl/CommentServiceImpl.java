package org.example.news.service.impl;

import org.example.news.db.entity.Comment;
import org.example.news.db.repository.CommentRepository;
import org.example.news.service.CommentService;
import org.example.news.service.core.UniversalServiceImpl;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends UniversalServiceImpl<Comment> implements CommentService {
  public CommentServiceImpl(CommentRepository commentRepository) {
    super(commentRepository, ErrorMsg.COMMENT_BY_ID_NOT_FOUND);
  }
}

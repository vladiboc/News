package org.example.news.service.impl;

import org.example.news.db.entity.Comment;
import org.example.news.db.repository.CommentRepository;
import org.example.news.service.CommentService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends AbstractUniversalService<Comment> implements CommentService {
  public CommentServiceImpl(CommentRepository commentRepository) {
    super(commentRepository, ErrorMsg.COMMENT_BY_ID_NOT_FOUND);
  }
}

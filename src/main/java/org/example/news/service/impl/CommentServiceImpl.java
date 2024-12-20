package org.example.news.service.impl;

import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.constant.ErrorMsg;
import org.example.news.db.entity.Comment;
import org.example.news.db.repository.CommentRepository;
import org.example.news.db.specification.CommentSpecification;
import org.example.news.service.CommentService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.web.dto.comment.CommentFilter;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl
    extends AbstractUniversalService<Comment, CommentFilter> implements CommentService {
  public CommentServiceImpl(CommentRepository commentRepository) {
    super(commentRepository, ErrorMsg.COMMENT_BY_ID_NOT_FOUND);
  }

  @Loggable
  @Override
  public List<Comment> findAllByFilter(CommentFilter filter) {
    return super.repository.findAll(CommentSpecification.withFilter(filter));
  }
}

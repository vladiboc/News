package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Comment;
import org.example.news.db.repository.CommentRepository;
import org.example.news.service.CommentService;
import org.example.news.util.BeanUtils;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
  private final CommentRepository commentRepository;

  @Override
  public List<Comment> findAll() {
    return commentRepository.findAll();
  }

  @Override
  public Comment findById(int id) {
    return commentRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            MessageFormat.format(ErrorMsg.COMMENT_BY_ID_NOT_FOUND, id))
        );
  }

  @Override
  public Comment save(Comment comment) {
    return commentRepository.save(comment);
  }

  @Override
  public Comment update(int id, Comment comment) {
    Comment existedComment = this.findById(id);
    BeanUtils.copyNonNullFields(comment, existedComment);
    existedComment.setId(id);
    return commentRepository.save(existedComment);
  }

  @Override
  public void deleteById(int id) {
    commentRepository.deleteById(id);
  }

}

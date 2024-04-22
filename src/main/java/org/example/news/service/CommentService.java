package org.example.news.service;

import org.example.news.db.entity.Comment;
import org.example.news.service.core.UniversalService;
import org.example.news.web.dto.comment.CommentFilter;

public interface CommentService extends UniversalService<Comment, CommentFilter> {
}

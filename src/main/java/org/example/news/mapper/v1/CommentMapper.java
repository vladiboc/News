package org.example.news.mapper.v1;

import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.Comment;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@DecoratedWith(CommentMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CommentMapper {
  Comment requestToComment(CommentUpsertRequest request);

  @Mapping(source = "comment.news.id", target = "newsId")
  @Mapping(source = "comment.user.id", target = "userId")
  CommentResponse commentToCommentResponse(Comment comment);

  List<CommentResponse> commentListToListOfCommentResponse(List<Comment> comments);

  @Loggable
  default CommentListResponse commentListToCommentListResponse(List<Comment> comments) {
    return new CommentListResponse(this.commentListToListOfCommentResponse(comments));
  }
}

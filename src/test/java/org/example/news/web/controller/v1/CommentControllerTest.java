package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.Category;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.CommentMapper;
import org.example.news.service.CommentService;
import org.example.news.util.TestStringUtil;
import org.example.news.web.controller.core.AbstractControllerTest;
import org.example.news.web.dto.comment.CommentFilter;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

class CommentControllerTest extends AbstractControllerTest {
  @MockBean
  CommentService commentService;
  @MockBean
  CommentMapper commentMapper;

  @Test
  void whenFindAllByFilter_thenReturnAllCommentsByFilter() throws Exception {
    final User user1 = new User(1, "Пользователь №1");
    final User user2 = new User(2, "Пользователь №2");
    final Category category1 = new Category(1, "Категория №1");
    final Category category2 = new Category(2, "Категория №2");
    final News news1 = new News(1, "Заголовок №1", "Новость №1", user1, category1);
    final News news2 = new News(2, "Заголовок №2", "Новость №2", user1, category2);
    final List<Comment> comments = new ArrayList<>();
    comments.add(new Comment(1, "Комментарий №1 Пользователя №1", news1, user1));
    comments.add(new Comment(2, "Комментарий №2 Пользователя №1", news2, user1));
    final CommentFilter filter = new CommentFilter(1);

    final List<CommentResponse> commentResponses = new ArrayList<>();
    commentResponses.add(new CommentResponse(1, "Комментарий №1 Пользователя №1", 1, 1));
    commentResponses.add(new CommentResponse(2, "Комментарий №2 Пользователя №1", 2, 1));

    final CommentListResponse commentListResponse = new CommentListResponse(commentResponses);

    Mockito.when(this.commentService.findAllByFilter(filter)).thenReturn(comments);
    Mockito.when((this.commentMapper.commentListToCommentListResponse(comments))).thenReturn(commentListResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/find_all_comments_response.json");
    final String actualResponse = this.mockGet("/api/v1/comment?userId=1", HttpStatus.OK);

    Mockito.verify(this.commentService, Mockito.times(1)).findAllByFilter(filter);
    Mockito.verify(this.commentMapper, Mockito.times(1)).commentListToCommentListResponse(comments);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenFindById_thenReturnCommentById() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final Category category = new Category(1, "Категория №1");
    final News news = new News(1, "Заголовок №1", "Новость №1", user, category);
    final Comment comment = new Comment(1, "Комментарий №1 Пользователя №1", news, user);
    final CommentResponse response = new CommentResponse(1, "Комментарий №1 Пользователя №1", 1,1);

    Mockito.when(this.commentService.findById(1)).thenReturn(comment);
    Mockito.when(this.commentMapper.commentToCommentResponse(comment)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/find_comment_by_id_response.json");
    final String actualResponse = this.mockGet("/api/v1/comment/1", HttpStatus.OK);

    Mockito.verify(this.commentService, Mockito.times(1)).findById(1);
    Mockito.verify(this.commentMapper, Mockito.times(1)).commentToCommentResponse(comment);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreate_thenReturnNewComment() throws Exception {
    final CommentUpsertRequest request = new CommentUpsertRequest("Комментарий №1 Пользователя №1", 1, 1);
    final User user = new User(1, "Пользователь №1");
    final Category category = new Category(1, "Категория №1");
    final News news = new News(1, "Заголовок №1", "Новость №1", user, category);
    final Comment requestedComment = new Comment("Комментарий №1 Пользователя №1", news, user);
    final Comment createdComment = new Comment(1, "Комментарий №1 Пользователя №1", news, user);
    final CommentResponse response = new CommentResponse(1, "Комментарий №1 Пользователя №1", 1, 1);

    Mockito.when(this.commentMapper.requestToComment(request)).thenReturn(requestedComment);
    Mockito.when(this.commentService.save(requestedComment)).thenReturn(createdComment);
    Mockito.when(this.commentMapper.commentToCommentResponse(createdComment)).thenReturn(response);

    final String actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.CREATED);
    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/create_comment_response.json");

    Mockito.verify(this.commentMapper, Mockito.times(1)).requestToComment(request);
    Mockito.verify(this.commentService, Mockito.times(1)).save(requestedComment);
    Mockito.verify(this.commentMapper, Mockito.times(1)).commentToCommentResponse(createdComment);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenUpdate_thenReturnUpdatedComment() throws Exception {
    final CommentUpsertRequest request = new CommentUpsertRequest("Исправленный Комментарий №1 Пользователя №1", 1, 1);
    final User user = new User(1, "Пользователь №1");
    final Category category = new Category(1, "Категория №1");
    final News news = new News(1, "Заголовок №1", "Новость №1", user, category);
    final Comment editedComment = new Comment("Исправленный Комментарий №1 Пользователя №1", news, user);
    final Comment updatedComment = new Comment(1, "Исправленный Комментарий №1 Пользователя №1", news, user);
    final CommentResponse response = new CommentResponse(1, "Исправленный Комментарий №1 Пользователя №1", 1, 1);

    Mockito.when(this.commentMapper.requestToComment(request)).thenReturn(editedComment);
    Mockito.when(this.commentService.update(1, editedComment)).thenReturn(updatedComment);
    Mockito.when(this.commentMapper.commentToCommentResponse(updatedComment)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/update_comment_response.json");
    final String actualResponse = this.mockPut("/api/v1/comment/1", request, HttpStatus.OK);

    Mockito.verify(this.commentMapper, Mockito.times(1)).requestToComment(request);
    Mockito.verify(this.commentService, Mockito.times(1)).update(1, editedComment);
    Mockito.verify(this.commentMapper, Mockito.times(1)).commentToCommentResponse(updatedComment);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenDeleteById_thenReturnNoContent() throws Exception {
    this.mockDelete("/api/v1/comment/1", HttpStatus.NO_CONTENT);

    Mockito.verify(this.commentService, Mockito.times(1)).deleteById(1);
  }

  @Test
  void whenFindByIdNotExistedComment_thenReturnError() throws Exception {
    Mockito.when(this.commentService.findById(999)).thenThrow(new EntityNotFoundException("Комментарий с id 999 не найден!"));

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_comment_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/comment/999", HttpStatus.NOT_FOUND);

    Mockito.verify(this.commentService, Mockito.times(1)).findById(999);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateCommentWithEmptyContent_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_empty_content_response.json");

    CommentUpsertRequest request = new CommentUpsertRequest(null, 1, 1);
    String actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setContent("  ");
    actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateCommentWithInvalidNewsId_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_invalid_news_id_response.json");

    CommentUpsertRequest request = new CommentUpsertRequest("Ценный комментарий", 0, 1);
    String actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setNewsId(-1);
    actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateCommentWithInvalidUserId_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_invalid_user_id_response.json");

    CommentUpsertRequest request = new CommentUpsertRequest("Ценный комментарий", 1, 0);
    String actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setUserId(-1);
    actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  void whenCreateCommentWithInvalidContentSize_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_comment_content_illegal_size_response.json");

    CommentUpsertRequest request = new CommentUpsertRequest("О", 1, 1);
    String actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setContent(RandomString.make(513));
    actualResponse = this.mockPost("/api/v1/comment", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }
}

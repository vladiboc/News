package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.Category;
import org.example.news.db.entity.Comment;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.CommentMapper;
import org.example.news.service.CommentService;
import org.example.news.util.TestStringUtil;
import org.example.news.web.dto.comment.CommentFilter;
import org.example.news.web.dto.comment.CommentListResponse;
import org.example.news.web.dto.comment.CommentResponse;
import org.example.news.web.dto.comment.CommentUpsertRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends AbstractControllerTest {
  @MockBean
  private CommentService commentService;
  @MockBean
  private CommentMapper commentMapper;

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindAllByFilter_thenReturnAllCommentsByFilter() throws Exception {
    final User user1 = new User(1, "Пользователь №1");
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
  @WithMockUser(username = "admin", roles = {"ADMIN"})
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
  @WithMockUser(username = "moder", roles = {"MODERATOR"})
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
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
      value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void whenUpdate_thenReturnUpdatedComment() throws Exception {
    final User user = this.userService.findByName("testUser");
    final CommentUpsertRequest request = new CommentUpsertRequest("Исправленный Комментарий №1 Пользователя №1", 1, user.getId());
    final Category category = new Category(1, "Категория №1");
    final News news = new News(1, "Заголовок №1", "Новость №1", user, category);
    final Comment editedComment = new Comment("Исправленный Комментарий №1 Пользователя №1", news, user);
    final Comment updatedComment = new Comment(1, "Исправленный Комментарий №1 Пользователя №1", news, user);
    final CommentResponse response = new CommentResponse(1, "Исправленный Комментарий №1 Пользователя №1", 1, 1);

    Mockito.when(this.commentMapper.requestToComment(request)).thenReturn(editedComment);
    Mockito.when(this.commentService.findById(1)).thenReturn(editedComment);
    Mockito.when(this.commentService.update(1, editedComment)).thenReturn(updatedComment);
    Mockito.when(this.commentMapper.commentToCommentResponse(updatedComment)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/update_comment_response.json");
    final String actualResponse = this.mockMvc.perform(put("/api/v1/comment/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.commentMapper, Mockito.times(1)).requestToComment(request);
    Mockito.verify(this.commentService, Mockito.times(1)).findById(1);
    Mockito.verify(this.commentService, Mockito.times(1)).update(1, editedComment);
    Mockito.verify(this.commentMapper, Mockito.times(1)).commentToCommentResponse(updatedComment);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenDeleteById_thenReturnNoContent() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final Comment comment = new Comment("Комментарий №1", user);

    this.mockMvc.perform(delete("/api/v1/comment/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(this.commentService, Mockito.times(1)).deleteById(1);
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindByIdNotExistedComment_thenReturnError() throws Exception {
    Mockito.when(this.commentService.findById(999)).thenThrow(new EntityNotFoundException("Комментарий с id 999 не найден!"));

    final String expectedResponse = TestStringUtil.readStringFromResource("response/comment/_err_comment_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/comment/999", HttpStatus.NOT_FOUND);

    Mockito.verify(this.commentService, Mockito.times(1)).findById(999);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
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
  @WithMockUser(username = "moderator", roles = {"MODERATOR"})
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
  @WithMockUser(username = "admin", roles = {"ADMIN"})
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
  @WithMockUser(username = "user", roles = {"USER"})
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

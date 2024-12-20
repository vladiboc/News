package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.Category;
import org.example.news.db.entity.News;
import org.example.news.db.entity.User;
import org.example.news.mapper.v1.NewsMapper;
import org.example.news.service.NewsService;
import org.example.news.util.TestStringUtil;
import org.example.news.web.dto.news.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class NewsControllerTest extends AbstractControllerTest {
  @MockBean
  private NewsService newsService;
  @MockBean
  private NewsMapper newsMapper;

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindAllByFilter_thenReturnAllNewsByFilter() throws Exception {
    final List<News> news = new ArrayList<>();
    final User user1 = new User(1, "Пользователь №1");
    final User user2 = new User(2, "Пользователь №2");
    final Category category1 = new Category(1, "Категория №1");
    final Category category2 = new Category(2, "Категория №2");
    news.add(new News(1, "Заголовок №1", "Новость №1", user1, category1));
    news.add(new News(2, "Заголовок №2", "Новость №2", user1, category2));
    news.add(new News(3, "Заголовок №3", "Новость №3", user2, category2));
    final NewsFilter filter = new NewsFilter(3, 0);

    final List<NewsResponseForList> newsResponses = new ArrayList<>();
    newsResponses.add(new NewsResponseForList(1, "Заголовок №1", "Новость №1", 1, 1, 0));
    newsResponses.add(new NewsResponseForList(2, "Заголовок №2", "Новость №2", 1, 2, 0));
    newsResponses.add(new NewsResponseForList(3, "Заголовок №3", "Новость №3", 2, 2, 0));

    final NewsListResponse newsListResponse = new NewsListResponse(newsResponses);

    Mockito.when(this.newsService.findAllByFilter(filter)).thenReturn(news);
    Mockito.when(this.newsMapper.newsListToNewsListResponse(news)).thenReturn(newsListResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/news/find_all_news_response.json");
    final String actualResponse = mockGet("/api/v1/news?pageSize=3&pageNumber=0", HttpStatus.OK);

    Mockito.verify(this.newsService, Mockito.times(1)).findAllByFilter(filter);
    Mockito.verify(this.newsMapper, Mockito.times(1)).newsListToNewsListResponse(news);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenFindById_thenReturnNewsById() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final Category category = new Category(1, "Категория №1");
    final News news = new News(1, "Заголовок №1", "Новость №1", user, category);
    final NewsResponse newsResponse = new NewsResponse(1, "Заголовок №1", "Новость №1", 1, 1);

    Mockito.when(this.newsService.findById(1)).thenReturn(news);
    Mockito.when(this.newsMapper.newsToNewsResponse(news)).thenReturn(newsResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/news/find_news_by_id_response.json");
    final String actualResponse = mockGet("/api/v1/news/1", HttpStatus.OK);

    Mockito.verify(this.newsService, Mockito.times(1)).findById(1);
    Mockito.verify(this.newsMapper, Mockito.times(1)).newsToNewsResponse(news);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "moderator", roles = {"MODERATOR"})
  void whenCreate_thenReturnNewNews() throws Exception {
    final NewsUpsertRequest request = new NewsUpsertRequest("Заголовок №1", "Новость №1", 1, 1);
    final User user = new User(1, "Пользователь №1");
    final Category category = new Category(1, "Категория №1");
    final News newNews = new News("Заголовок №1", "Новость №1", user, category);
    final News createdNews = new News(1, "Заголовок №1", "Новость №1", user, category);
    final NewsResponse response = new NewsResponse(1, "Заголовок №1", "Новость №1", 1, 1);

    Mockito.when(this.newsMapper.requestToNews(request)).thenReturn(newNews);
    Mockito.when(this.newsService.save(newNews)).thenReturn(createdNews);
    Mockito.when(this.newsMapper.newsToNewsResponse(createdNews)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/news/create_news_response.json");
    final String actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.CREATED);

    Mockito.verify(this.newsMapper, Mockito.times(1)).requestToNews(request);
    Mockito.verify(this.newsService, Mockito.times(1)).save(newNews);
    Mockito.verify(this.newsMapper, Mockito.times(1)).newsToNewsResponse(createdNews);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithUserDetails(userDetailsServiceBeanName = "userDetailsServiceImpl",
      value = "testUser", setupBefore = TestExecutionEvent.TEST_EXECUTION)
  void whenUpdate_thenReturnUpdatedNews() throws Exception {
    final var user = this.userService.findByName("testUser");
    final NewsUpsertRequest request = new NewsUpsertRequest("Заголовок №1", "Новость №1 обновлённая", user.getId(), 1);
    final Category category = new Category(1, "Категория №1");
    final News editedNews = new News("Заголовок №1", "Новость №1 обновлённая", user, category);
    final News updatedNews = new News(1, "Заголовок №1", "Новость №1 обновлённая", user, category);
    final NewsResponse response = new NewsResponse(1, "Заголовок №1", "Новость №1 обновлённая", 1, 1);

    Mockito.when(this.newsMapper.requestToNews(request)).thenReturn(editedNews);
    Mockito.when(this.newsService.findById(1)).thenReturn(editedNews);
    Mockito.when(this.newsService.update(1, editedNews)).thenReturn(updatedNews);
    Mockito.when(this.newsMapper.newsToNewsResponse(updatedNews)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/news/update_news_response.json");
    final String actualResponse = this.mockMvc.perform(put("/api/v1/news/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(this.objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andReturn()
        .getResponse()
        .getContentAsString(StandardCharsets.UTF_8);

    Mockito.verify(this.newsMapper, Mockito.times(1)).requestToNews(request);
    Mockito.verify(this.newsService, Mockito.times(1)).findById(1);
    Mockito.verify(this.newsService, Mockito.times(1)).update(1, editedNews);
    Mockito.verify(this.newsMapper, Mockito.times(1)).newsToNewsResponse(updatedNews);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenDeleteById_thenReturnNoContent() throws Exception {
    final User user = new User(1, "Пользователь №1");
    final News news = new News("Заголовок №1", "Новость №1", user);

    Mockito.when(this.newsService.findById(1)).thenReturn(news);

    this.mockMvc.perform(delete("/api/v1/news/1"))
        .andExpect(status().isNoContent());

    Mockito.verify(this.newsService, Mockito.times(1)).deleteById(1);
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindByIdNotExistedNews_thenReturnError() throws Exception {
    Mockito.when(this.newsService.findById(888)).thenThrow(new EntityNotFoundException("Новость с id 888 не найдена!"));

    final String expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_news_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/news/888", HttpStatus.NOT_FOUND);

    Mockito.verify(this.newsService, Mockito.times(1)).findById(888);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenCreateNewsWithEmptyField_thenReturnError() throws Exception {
    final NewsUpsertRequest request = new NewsUpsertRequest("Заголовок №1","Новость №1",1, 1);

    request.setTitle(null);
    String expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_news_empty_title_response.json");
    String actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setTitle("Заголовок №1");
    request.setContent(null);
    expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_news_empty_content_response.json");
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenCreateWithInvalidIds_thenReturnError() throws Exception {
    final NewsUpsertRequest request = new NewsUpsertRequest("Заголовок №1","Новость №1",0, 1);
    String expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_invalid_user_id_response.json");

    String actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setUserId(-1);
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_invalid_category_id_response.json");
    request.setUserId(1);
    request.setCategoryId(0);
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setCategoryId(-1);
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "moderator", roles = {"MODERATOR"})
  void whenCreateNewsWithIllegalSizeOfTitleOrContent_thenReturnError() throws Exception {
    final NewsUpsertRequest request = new NewsUpsertRequest("Заголовок №1","Новость №1",1, 1);
    String expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_news_title_illegal_size_response.json");

    request.setTitle(RandomString.make(1));
    String actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setTitle(RandomString.make(65));
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    expectedResponse = TestStringUtil.readStringFromResource("response/news/_err_news_content_illegal_size_response.json");
    request.setTitle("Заголовок №1");

    request.setContent(RandomString.make(1));
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setContent(RandomString.make(2049));
    actualResponse = this.mockPost("/api/v1/news", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }
}

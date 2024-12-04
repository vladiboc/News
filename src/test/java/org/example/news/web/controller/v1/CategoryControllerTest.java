package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import net.javacrumbs.jsonunit.JsonAssert;
import org.example.news.db.entity.Category;
import org.example.news.mapper.v1.CategoryMapper;
import org.example.news.service.CategoryService;
import org.example.news.util.TestStringUtil;
import org.example.news.web.dto.category.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import org.springframework.security.test.context.support.WithMockUser;

class CategoryControllerTest extends AbstractControllerTest {
  @MockBean
  private CategoryService categoryService;
  @MockBean
  private CategoryMapper categoryMapper;

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindAllByFilter_thenReturnAllCategoriesByFilter() throws Exception {
    final List<Category> categories = new ArrayList<>();
    categories.add(new Category("Категория №1"));
    categories.add(new Category("Категория №2"));
    final CategoryFilter filter = new CategoryFilter(3, 0);

    final List<CategoryResponseForList> categoryResponses = new ArrayList<>();
    categoryResponses.add(new CategoryResponseForList(1, "Категория №1", 0));
    categoryResponses.add(new CategoryResponseForList(2, "Категория №2", 0));

    final CategoryListResponse response = new CategoryListResponse(categoryResponses);

    Mockito.when(this.categoryService.findAllByFilter(filter)).thenReturn(categories);
    Mockito.when(this.categoryMapper.categoryListToCategoryListResponse(categories)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/find_all_categories_response.json");
    final String actualResponse = this.mockGet("/api/v1/category?pageSize=3&pageNumber=0", HttpStatus.OK);

    Mockito.verify(this.categoryService, Mockito.times(1)).findAllByFilter(filter);
    Mockito.verify(this.categoryMapper, Mockito.times(1)).categoryListToCategoryListResponse(categories);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindById_thenReturnCategoryById() throws Exception {
    final Category category = new Category(1, "Категория №1");
    final CategoryResponse categoryResponse = new CategoryResponse(1, "Категория №1");

    Mockito.when(this.categoryService.findById(1)).thenReturn(category);
    Mockito.when(this.categoryMapper.categoryToCategoryResponse(category)).thenReturn(categoryResponse);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/find_category_by_id_response.json");
    final String actualResponse = this.mockGet("/api/v1/category/1", HttpStatus.OK);

    Mockito.verify(this.categoryService, Mockito.times(1)).findById(1);
    Mockito.verify(this.categoryMapper, Mockito.times(1)).categoryToCategoryResponse(category);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenCreate_thenReturnNewCategory() throws Exception {
    final CategoryUpsertRequest request = new CategoryUpsertRequest("Категория №1");
    final Category newCategory = new Category(1, "Категория №1");
    final Category createdCategory = new Category(1, "Категория №1");
    final CategoryResponse response = new CategoryResponse(1, "Категория №1");

    Mockito.when(this.categoryMapper.requestToCategory(request)).thenReturn(newCategory);
    Mockito.when(this.categoryService.save(newCategory)).thenReturn(createdCategory);
    Mockito.when(this.categoryMapper.categoryToCategoryResponse(createdCategory)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/create_category_response.json");
    final String actualResponse = this.mockPost("/api/v1/category", request, HttpStatus.CREATED);

    Mockito.verify(this.categoryMapper, Mockito.times(1)).requestToCategory(request);
    Mockito.verify(this.categoryService, Mockito.times(1)).save(newCategory);
    Mockito.verify(this.categoryMapper, Mockito.times(1)).categoryToCategoryResponse(createdCategory);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "moderator", roles = {"MODERATOR"})
  void whenUpdate_thenReturnUpdatedCategory() throws Exception {
    final CategoryUpsertRequest request = new CategoryUpsertRequest("Категория №1 обновлённая");
    final Category editedCategory = new Category("Категория №1 обновлённая");
    final Category updatedCategory = new Category(1, "Категория №1 обновлённая");
    final CategoryResponse response = new CategoryResponse(1, "Категория №1 обновлённая");

    Mockito.when(this.categoryMapper.requestToCategory(request)).thenReturn(editedCategory);
    Mockito.when(this.categoryService.update(1, editedCategory)).thenReturn(updatedCategory);
    Mockito.when(this.categoryMapper.categoryToCategoryResponse(updatedCategory)).thenReturn(response);

    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/update_category_response.json");
    final String actualResponse = this.mockPut("/api/v1/category/1", request, HttpStatus.OK);

    Mockito.verify(this.categoryMapper, Mockito.times(1)).requestToCategory(request);
    Mockito.verify(this.categoryService, Mockito.times(1)).update(1, editedCategory);
    Mockito.verify(this.categoryMapper, Mockito.times(1)).categoryToCategoryResponse(updatedCategory);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenDeleteById_thenReturnNoContent() throws Exception {
    this.mockDelete("/api/v1/category/1", HttpStatus.NO_CONTENT);

    Mockito.verify(this.categoryService, Mockito.times(1)).deleteById(1);
  }

  @Test
  @WithMockUser(username = "user", roles = {"USER"})
  void whenFindByIdNotExistedCategory_thenReturnError() throws Exception {
    Mockito.when(this.categoryService.findById(777)).thenThrow(new EntityNotFoundException("Категория с id 777 не найдена!"));

    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/_err_category_by_id_not_found_response.json");
    final String actualResponse = this.mockGet("/api/v1/category/777", HttpStatus.NOT_FOUND);

    Mockito.verify(this.categoryService, Mockito.times(1)).findById(777);

    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "admin", roles = {"ADMIN"})
  void whenCreateCategoryWithEmptyName_thenReturnError() throws Exception {
    final CategoryUpsertRequest request = new CategoryUpsertRequest();
    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/_err_empty_category_name_response.json");
    final String actualResponse = this.mockPost("/api/v1/category", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }

  @Test
  @WithMockUser(username = "moder", roles = {"MODERATOR"})
  void whenCreateCategoryWithInvalidContentSize_thenReturnError() throws Exception {
    final String expectedResponse = TestStringUtil.readStringFromResource("response/category/_err_category_content_illegal_size_response.json");

    CategoryUpsertRequest request = new CategoryUpsertRequest(RandomString.make(2));
    String actualResponse = this.mockPost("/api/v1/category", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);

    request.setName(RandomString.make(33));
    actualResponse = this.mockPost("/api/v1/category", request, HttpStatus.BAD_REQUEST);
    JsonAssert.assertJsonEquals(expectedResponse, actualResponse);
  }
}

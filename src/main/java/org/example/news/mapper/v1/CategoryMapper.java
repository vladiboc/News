package org.example.news.mapper.v1;

import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.db.entity.Category;
import org.example.news.web.dto.category.CategoryListResponse;
import org.example.news.web.dto.category.CategoryResponse;
import org.example.news.web.dto.category.CategoryResponseForList;
import org.example.news.web.dto.category.CategoryUpsertRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    uses = {NewsMapper.class})
public interface CategoryMapper {
  Category requestToCategory(CategoryUpsertRequest request);

  CategoryResponse categoryToCategoryResponse(Category category);

  @Loggable
  default CategoryResponseForList categoryToCategoryForList(Category category) {
    return new CategoryResponseForList(
        category.getId(),
        category.getName(),
        category.getNews().size()
    );
  }

  List<CategoryResponseForList> categoryListToListOfCategoryResponse(List<Category> categories);

  @Loggable
  default CategoryListResponse categoryListToCategoryListResponse(List<Category> categories) {
    return new CategoryListResponse(this.categoryListToListOfCategoryResponse(categories));
  }
}

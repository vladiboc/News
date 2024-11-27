package org.example.news.service.impl;

import java.util.List;
import org.example.news.aop.loggable.Loggable;
import org.example.news.constant.ErrorMsg;
import org.example.news.db.entity.Category;
import org.example.news.db.repository.CategoryRepository;
import org.example.news.service.CategoryService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.web.dto.category.CategoryFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl
    extends AbstractUniversalService<Category, CategoryFilter> implements CategoryService {

  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    super(categoryRepository, ErrorMsg.CATEGORY_BY_ID_NOT_FOUND);
  }

  @Loggable
  @Override
  public List<Category> findAllByFilter(CategoryFilter filter) {

    return super.repository.findAll(
        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
    ).getContent();
  }
}

package org.example.news.service.impl;

import org.example.news.db.entity.Category;
import org.example.news.db.entity.Comment;
import org.example.news.db.repository.CategoryRepository;
import org.example.news.service.CategoryService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.ErrorMsg;
import org.example.news.web.dto.category.CategoryFilter;
import org.example.news.web.dto.comment.CommentFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl extends AbstractUniversalService<Category, CategoryFilter> implements CategoryService {
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    super(categoryRepository, ErrorMsg.CATEGORY_BY_ID_NOT_FOUND);
  }

  @Override
  public List<Category> findAllByFilter(CategoryFilter filter) {
    return super.repository.findAll(
        PageRequest.of(filter.getPageNumber(), filter.getPageSize())
    ).getContent();
  }
}

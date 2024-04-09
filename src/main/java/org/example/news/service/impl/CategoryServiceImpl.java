package org.example.news.service.impl;

import org.example.news.db.entity.Category;
import org.example.news.db.repository.CategoryRepository;
import org.example.news.service.CategoryService;
import org.example.news.service.core.AbstractUniversalService;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends AbstractUniversalService<Category> implements CategoryService {
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    super(categoryRepository, ErrorMsg.CATEGORY_BY_ID_NOT_FOUND);
  }
}

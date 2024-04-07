package org.example.news.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.news.db.entity.Category;
import org.example.news.db.repository.CategoryRepository;
import org.example.news.service.CategoryService;
import org.example.news.service.core.UniversalServiceImpl;
import org.example.news.util.BeanUtils;
import org.example.news.util.ErrorMsg;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;

@Service
public class CategoryServiceImpl extends UniversalServiceImpl<Category> implements CategoryService {
  public CategoryServiceImpl(CategoryRepository categoryRepository) {
    super(categoryRepository, ErrorMsg.CATEGORY_BY_ID_NOT_FOUND);
  }
}

package org.example.news.service;

import org.example.news.db.entity.Category;
import org.example.news.service.core.UniversalService;
import org.example.news.web.dto.category.CategoryFilter;

import java.util.List;

public interface CategoryService extends UniversalService<Category, CategoryFilter> {
}

package org.example.news.db.repository;

import org.example.news.db.entity.Category;
import org.example.news.service.core.AbstractUniversalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CategoryRepository extends
    JpaRepository<Category, Integer>,
    JpaSpecificationExecutor<Category>,
    AbstractUniversalService.UniversalRepository<Category> {
}

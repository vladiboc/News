package org.example.news.db.repository;

import org.example.news.db.entity.News;
import org.example.news.service.core.AbstractUniversalService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface NewsRepository extends
    JpaRepository<News, Integer>,
    JpaSpecificationExecutor<News>,
    AbstractUniversalService.UniversalRepository<News> {
}

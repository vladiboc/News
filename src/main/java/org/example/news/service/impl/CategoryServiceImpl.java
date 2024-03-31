//package org.example.news.service.impl;
//
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.example.news.db.entity.Category;
//import org.example.news.db.repository.CategoryRepository;
//import org.example.news.service.CategoryService;
//import org.example.news.util.BeanUtils;
//import org.springframework.stereotype.Service;
//
//import java.text.MessageFormat;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CategoryServiceImpl implements CategoryService {
//  private final CategoryRepository categoryRepository;
//
//  @Override
//  public List<Category> findAll() {
//    return categoryRepository.findAll();
//  }
//
//  // TODO заменить и обратотать исключение
//  @Override
//  public Category findById(int id) {
//    return categoryRepository.findById(id)
//        .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
//            "Пользователь с id {0} не найден!", id
//        )));
//  }
//
//  @Override
//  public Category save(Category category) {
//    return categoryRepository.save(category);
//  }
//
//  @Override
//  public Category update(int id, Category category) {
//    Category existedCategory = this.findById(category.getId());
//    BeanUtils.copyNonNullFields(category, existedCategory);
//    return categoryRepository.save(existedCategory);
//  }
//
//  @Override
//  public void deleteById(int id) {
//    categoryRepository.deleteById(id);
//  }
//
//}

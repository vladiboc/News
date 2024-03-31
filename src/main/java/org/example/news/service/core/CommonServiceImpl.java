package org.example.news.service.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.text.MessageFormat;
import java.util.List;

@RequiredArgsConstructor
public abstract class CommonServiceImpl<T> implements CommonService<T> {
  private final JpaRepository<T, Integer> repository;

  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  // TODO заменить и обратотать исключение
  @Override
  public T findById(int id) {
    return repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format(
            "Пользователь с id {0} не найден!", id
        )));
  }

  @Override
  public T save(T object) {
    return repository.save(object);
  }

  @Override
  public T update(T object) {
//    T existedT = this.findById(object.getId());
//    BeanUtils.copyNonNullFields(object, existedT);
//    return repository.save(existedT);
    return repository.save(object);
  }

  @Override
  public void deleteById(int id) {
    repository.deleteById(id);
  }

}

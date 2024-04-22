package org.example.news.service.core;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.news.db.entity.core.Identifiable;
import org.example.news.util.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public abstract class AbstractUniversalService<T extends Identifiable, F> implements UniversalService<T, F> {
  public interface UniversalRepository<T> extends JpaRepository<T, Integer>, JpaSpecificationExecutor<T> {}

  protected final UniversalRepository<T> repository;
  protected final String notFoundByIdMsg;

  @Override
  public List<T> findAll() {
    return this.repository.findAll();
  }

  @Override
  public T findById(int id) {
    return this.repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException(
            MessageFormat.format(this.notFoundByIdMsg, id)
          )
        );
  }

  @Override
  public T save(T object) {
    return this.repository.save(object);
  }

  @Override
  public T update(int id, T object) {
    T existedObject = this.findById(id);
    BeanUtils.copyNonNullFields(object, existedObject);
    existedObject.setId(id);
    return this.repository.save(existedObject);
  }

  @Override
  public void deleteById(int id) {
    this.repository.deleteById(id);
  }
}

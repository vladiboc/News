package org.example.news.util;

import java.lang.reflect.Field;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.example.news.aop.loggable.Loggable;

@UtilityClass
public class BeanUtils {
  @SneakyThrows
  @Loggable
  public void copyNonNullFields(Object source, Object destination) {
    Class<?> clazz = source.getClass();
    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      Object value = field.get(source);

      if (value != null) {
        field.set(destination, value);
      }
    }
  }
}

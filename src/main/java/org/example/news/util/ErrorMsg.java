package org.example.news.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ErrorMsg {
  public static final String USER_BY_ID_NOT_FOUND = "Пользователь с id {0} не найден!";
  public static final String USER_NAME_MUST_BE_FILLED = "Имя пользователя должно быть заполнено!";
  public static final String USER_NAME_SIZE_FROM_MIN_TO_MAX = "Имя пользователя должно быть от {min} до {max} символов!";
  public static final String COMMENT_BY_ID_NOT_FOUND = "Комментарий с id {0} не найден!";
  public static final String COMMENT_CONTENT_MUST_BE_FILLED = "Комментарий должен быть не пустым!";
  public static final String COMMENT_SIZE_FROM_MIN_TO_MAX = "Длина комментария должна быть от {min} до {max} символов!";
  public static final String COMMENT_USER_ID_MUST_BE_POSITIVE = "Идентификатор автора комментария должен быть целым числом больше ноля!";
}

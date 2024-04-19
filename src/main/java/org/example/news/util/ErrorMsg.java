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
  public static final String COMMENT_NEWS_ID_MUST_BE_POSITIVE = "Идентификатор комментируемой новости должен быть целым числом больше ноля!";
  public static final String COMMENT_USER_ID_MUST_BE_POSITIVE = "Идентификатор автора комментария должен быть целым числом больше ноля!";
  public static final String CATEGORY_BY_ID_NOT_FOUND = "Категория с id {0} не найдена!";
  public static final String CATEGORY_NAME_MUST_BE_FILLED = "Название категории должно быть заполнено!";
  public static final String CATEGORY_NAME_SIZE_FROM_MIN_TO_MAX = "Название категории должно быть от {min} до {max} символов!";
  public static final String NEWS_BY_ID_NOT_FOUND = "Новость с id {0} не найдена!";
  public static final String NEWS_USER_ID_MUST_BE_POSITIVE = "Идентификатор автора новости должен быть целым числом больше ноля!";
  public static final String NEWS_CATEGORY_MUST_BE_POSITIVE = "Идентификатор категории новости должен быть целым числом больше ноля!";
  public static final String NEWS_TITLE_SIZE_FROM_MIN_TO_MAX = "Заголовок новости должен быть от {min} до {max} символов!";
  public static final String NEWS_TITLE_MUST_BE_FILLED = "Заголовок новости должен быть не пустым!";
  public static final String NEWS_CONTENT_MUST_BE_FILLED = "Содержание новости должен быть заполнено!";
  public static final String NEWS_CONTENT_SIZE_FROM_MIN_TO_MAX = "Содержание новости должен быть от {min} до {max} символов!";
}

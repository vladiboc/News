/**
 * Здесь собраны все константы - размеры строк для валидации
 */
package org.example.news.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringSizes {
  public static final int USER_NAME_MIN = 2;
  public static final int USER_NAME_MAX = 32;
  public static final int COMMENT_CONTENT_MIN = 2;
  public static final int COMMENT_CONTENT_MAX = 512;
  public static final int CATEGORY_NAME_MIN = 3;
  public static final int CATEGORY_NAME_MAX = 32;
  public static final int NEWS_TITLE_MIN = 2;
  public static final int NEWS_TITLE_MAX = 64;
  public static final int NEWS_CONTENT_MIN = 2;
  public static final int NEWS_CONTENT_MAX = 2048;
}

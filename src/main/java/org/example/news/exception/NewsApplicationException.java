package org.example.news.exception;

public class NewsApplicationException extends Exception {
  public NewsApplicationException() {
  }

  public NewsApplicationException(String message) {
    super(message);
  }

  public NewsApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public NewsApplicationException(Throwable cause) {
    super(cause);
  }

  public NewsApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

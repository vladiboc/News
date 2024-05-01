package org.example.news.exception;

public class UserUnmatchedException extends NewsApplicationException {
  public UserUnmatchedException() {
  }

  public UserUnmatchedException(String message) {
    super(message);
  }

  public UserUnmatchedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserUnmatchedException(Throwable cause) {
    super(cause);
  }

  public UserUnmatchedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

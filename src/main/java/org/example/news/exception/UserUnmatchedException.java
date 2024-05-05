package org.example.news.exception;

public class UserUnmatchedException extends NewsApplicationException {
  public UserUnmatchedException() {
  }

  public UserUnmatchedException(String message) {
    super(message);
  }
}

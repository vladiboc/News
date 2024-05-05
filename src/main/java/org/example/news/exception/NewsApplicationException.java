package org.example.news.exception;

public class NewsApplicationException extends Exception {
  public NewsApplicationException() {
  }

  public NewsApplicationException(String message) {
    super(message);
  }
}

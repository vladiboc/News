package org.example.news.web.controller.v1;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.example.news.exception.UserUnmatchedException;
import org.example.news.constant.ErrorMsg;
import org.example.news.web.dto.error.ErrorMsgResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {
  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorMsgResponse> resourceNotFound(NoResourceFoundException e) {

    log.error("ExceptionHandlerController.resourceNotFound: {}", ErrorMsg.NO_RESOURCE_FOUND, e);

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMsgResponse(ErrorMsg.NO_RESOURCE_FOUND));
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorMsgResponse> notFound(EntityNotFoundException e) {

    log.error("ExceptionHandlerController.notFound:", e);

    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ErrorMsgResponse(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorMsgResponse> badRequest(MethodArgumentNotValidException e) {

    log.error("ExceptionHandlerController.badRequest:", e);

    BindingResult bindingResult = e.getBindingResult();
    List<String> errorMessages = bindingResult.getAllErrors()
        .stream()
        .map(DefaultMessageSourceResolvable::getDefaultMessage)
        .toList();
    String errorMessage = String.join("; ", errorMessages);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ErrorMsgResponse(errorMessage));
  }

  @ExceptionHandler(UserUnmatchedException.class)
  public ResponseEntity<ErrorMsgResponse> illegalUser(UserUnmatchedException e) {

    log.error("ExceptionHandlerController.badRequest:", e);

    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(new ErrorMsgResponse(e.getMessage()));
  }
}

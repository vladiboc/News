/**
 * Аспект для аннотаций:
 * @MatchableNewsUser - проверка пользователя, создавшего новость
 * @MatchableCommentUser - проверка пользоваателя, создавшего комментарий
 */
package org.example.news.aop.matchable;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.util.Base64;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.news.exception.UserUnmatchedException;
import org.example.news.service.CommentService;
import org.example.news.service.NewsService;
import org.example.news.constant.ErrorMsg;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class MatchingUserAspect {
  public static final String HTTP_HEADER_USER_ID = "X-User-Id";
  private final NewsService newsService;
  private final CommentService commentService;
  private final UserDetailsService userDetailsService;

  @Before("@annotation(MatchableUser)")
  public void matchingUser(
  ) throws AccessDeniedException {
    HttpServletRequest request = this.getRequest();
    var header = request.getHeader("Authorization");
    var encodedLoginPassword = header.substring("Basic ".length());
    var loginPassword = new String(Base64.getDecoder().decode(encodedLoginPassword), StandardCharsets.UTF_8);
    var ind = loginPassword.indexOf(":");
    var login = loginPassword.substring(0, ind);
    var userDetails = userDetailsService.loadUserByUsername(login);
    userDetails.getAuthorities();
//    if (headerUserId != newsUserId) {
//      throw new AccessDeniedException(ErrorMsg.USER_ID_NOT_MATCHED);
//    }
  }

  @Before("@annotation(MatchableNewsUser)")
  public void matchingNewsUser() throws UserUnmatchedException {
    HttpServletRequest request = this.getRequest();
    int headerUserId = this.getHeaderId(request);
    int newsId = this.getPathId(request);
    int newsUserId = this.newsService.findById(newsId).getUser().getId();
    if (headerUserId != newsUserId) {
      throw new UserUnmatchedException(ErrorMsg.NEWS_USER_ILLEGAL);
    }
  }

  @Before("@annotation(MatchableCommentUser)")
  public void matchingCommentUser() throws UserUnmatchedException {
    HttpServletRequest request = this.getRequest();
    int headerUserId = this.getHeaderId(request);
    int commentId = this.getPathId(request);
    int commentUserId = this.commentService.findById(commentId).getUser().getId();
    if (headerUserId != commentUserId) {
      throw new UserUnmatchedException(ErrorMsg.COMMENT_USER_ILLEGAL);
    }
  }

  private HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    return  ((ServletRequestAttributes) requestAttributes).getRequest();
  }

  private int getPathId(HttpServletRequest request) {
    var pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    return Integer.valueOf(pathVariables.get("id"));
  }

  /**
   * Получить id пользователя, переданный через заголовок запроса
   * @param request - объект запроса
   * @return - id пользователя из заголовка запроса
   * @throws UserUnmatchedException - при отсутствии заголовка с id или при отрицательном id в заголовке
   */
  private int getHeaderId(HttpServletRequest request) throws UserUnmatchedException {
    String headerValue = request.getHeader(MatchingUserAspect.HTTP_HEADER_USER_ID);
    try {
      int id = Integer.valueOf(headerValue);
      if (id <= 0) {
        throw new UserUnmatchedException(ErrorMsg.HEADER_USER_ID_NOT_POSITIVE);
      }
      return id;
    } catch (NumberFormatException e) {
      throw new UserUnmatchedException(ErrorMsg.HEADER_USER_ILLEGAL);
    }
  }
}

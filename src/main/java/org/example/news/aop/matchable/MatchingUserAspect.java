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
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.example.news.constant.ErrorMsg;
import org.example.news.db.entity.Role;
import org.example.news.db.entity.User;
import org.example.news.exception.UserUnmatchedException;
import org.example.news.service.CommentService;
import org.example.news.service.NewsService;
import org.example.news.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;
import static org.example.news.db.entity.RoleType.ROLE_ADMIN;
import static org.example.news.db.entity.RoleType.ROLE_MODERATOR;
import static org.example.news.db.entity.RoleType.ROLE_USER;

@Aspect
@Component
@RequiredArgsConstructor
public class MatchingUserAspect {
  public static final String HTTP_HEADER_USER_ID = "X-User-Id";
  private static final String BASIC_PREFIX = "Basic ";
  private final NewsService newsService;
  private final CommentService commentService;
  private final UserService userService;

  @Before("@annotation(MatchableUser)")
  public void matchingUser() throws AuthorizationDeniedException {
    final var requester = this.getRequestAuthor();
    var isMatched = true;
    for (Role role : requester.getRoles()) {
      if (ROLE_ADMIN.equals(role.getAuthority()) || ROLE_MODERATOR.equals(role.getAuthority())) {
        isMatched = true;
        break;
      }
      if (ROLE_USER.equals(role.getAuthority()) && requester.getId() != this.getPathId()) {
        isMatched = false;
      }
    }
    if (!isMatched) {
      throw new AuthorizationDeniedException(ErrorMsg.USER_ID_NOT_MATCHED, () -> false);
    }
  }

  @Before("@annotation(MatchableNewsUser)")
  public void matchingNewsUser() throws UserUnmatchedException {
    HttpServletRequest request = this.getRequest();
    int headerUserId = this.getHeaderId(request);
    int newsId = this.getPathId();
    int newsUserId = this.newsService.findById(newsId).getUser().getId();
    if (headerUserId != newsUserId) {
      throw new UserUnmatchedException(ErrorMsg.NEWS_USER_ILLEGAL);
    }
  }

  @Before("@annotation(MatchableCommentUser)")
  public void matchingCommentUser() throws UserUnmatchedException {
    HttpServletRequest request = this.getRequest();
    int headerUserId = this.getHeaderId(request);
    int commentId = this.getPathId();
    int commentUserId = this.commentService.findById(commentId).getUser().getId();
    if (headerUserId != commentUserId) {
      throw new UserUnmatchedException(ErrorMsg.COMMENT_USER_ILLEGAL);
    }
  }

  private HttpServletRequest getRequest() {
    RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
    return  ((ServletRequestAttributes) requestAttributes).getRequest();
  }

  private User getRequestAuthor() {
    final var encodedLoginPassword = this.getRequest()
        .getHeader(HttpHeaders.AUTHORIZATION)
        .substring(BASIC_PREFIX.length());
    final var loginPassword = new String(
        Base64.getDecoder().decode(encodedLoginPassword), StandardCharsets.UTF_8);
    final var login = loginPassword.substring(0, loginPassword.indexOf(":"));

    return this.userService.findByName(login);
  }

  private int getPathId() {
    final var request = this.getRequest();
    final var pathVariables =
        (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
    return Integer.parseInt(pathVariables.get("id"));
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
      int id = Integer.parseInt(headerValue);
      if (id <= 0) {
        throw new UserUnmatchedException(ErrorMsg.HEADER_USER_ID_NOT_POSITIVE);
      }
      return id;
    } catch (NumberFormatException e) {
      throw new UserUnmatchedException(ErrorMsg.HEADER_USER_ILLEGAL);
    }
  }
}

package org.example.news.web.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.news.constant.ErrorMsg;
import org.example.news.constant.StringSizes;
import org.example.news.db.entity.RoleType;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpsertRequest {
  @NotBlank(message = ErrorMsg.USER_NAME_MUST_BE_FILLED)
  @Size(
      min = StringSizes.USER_NAME_MIN,
      max = StringSizes.USER_NAME_MAX,
      message = ErrorMsg.USER_NAME_SIZE_FROM_MIN_TO_MAX
  )
  String name;


  @NotBlank(message = ErrorMsg.USER_PASSWORD_MUST_BE_SET)
  @Size(
      min = StringSizes.USER_PASSWORD_MIN,
      max = StringSizes.USER_PASSWORD_MAX,
      message = ErrorMsg.USER_PASSWORD_SIZE_FROM_MIN_TO_MAX
  )
  private String password;

  @NotNull(message = ErrorMsg.USER_ROLES_MUST_BE_SET)
  private Set<RoleType> roles;
}

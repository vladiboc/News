package org.example.news.mapper.v1;

import java.util.List;
import org.example.news.db.entity.Role;
import org.example.news.db.entity.RoleType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
  default RoleType roleToRoleType(Role role) {
    return role.getAuthority();
  }

  List<RoleType> roleListToRoleTypeList(List<Role> roles);
}

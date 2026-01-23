package org.spring.security.demo.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class RolePermissionResponse {

    private Long roleId;
    private String roleName;
    private Set<String> permissions;
}

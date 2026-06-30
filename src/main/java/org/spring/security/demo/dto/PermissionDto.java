package org.spring.security.demo.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermissionDto {
    private Long id;
    private String code;
    private String description;
}

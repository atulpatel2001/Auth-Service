package org.spring.security.demo.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class AuthUserDto {
    private Long id;
    private String email;
    private String phoneNumber;
    private Set<String> roles;
    private Set<String> permissions;

}


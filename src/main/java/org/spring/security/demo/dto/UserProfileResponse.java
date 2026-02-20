package org.spring.security.demo.dto;

import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {

    private Long id;
    private String phoneNumber;
    private String email;
    private Set<String> roles;        // ['ADMIN', 'USER']
    private Set<String> permissions;

}

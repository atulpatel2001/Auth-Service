package org.spring.security.demo.service;

import org.spring.security.demo.dto.AssignUserRoleRequest;
import org.spring.security.demo.dto.RegisterUserRequest;
import org.spring.security.demo.dto.RemoveUserRoleRequest;

public interface UserService {


    void assignRoleToUser(AssignUserRoleRequest request);

    void removeRoleFromUser(RemoveUserRoleRequest request);
}


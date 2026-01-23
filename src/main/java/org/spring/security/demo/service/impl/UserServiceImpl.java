package org.spring.security.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.spring.security.demo.dto.AssignUserRoleRequest;
import org.spring.security.demo.dto.RemoveUserRoleRequest;
import org.spring.security.demo.exception.BusinessException;
import org.spring.security.demo.exception.ResourceNotFoundException;
import org.spring.security.demo.model.ERole;
import org.spring.security.demo.model.EUser;
import org.spring.security.demo.repository.jpa.RoleRepository;
import org.spring.security.demo.repository.jpa.UserRepository;
import org.spring.security.demo.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {



    private final UserRepository userRepository;
    private final RoleRepository roleRepository;




    // ================= ASSIGN ROLE =================

    @Override
    public void assignRoleToUser(AssignUserRoleRequest request) {

        EUser user = userRepository.findByIdAndIsDeletedFalse(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ERole role = roleRepository.findByIdAndIsDeletedFalse(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        // Prevent duplicate role
        if (user.getRoles().contains(role)) {
            throw new BusinessException("Role already assigned to user", HttpStatus.BAD_REQUEST);
        }

        user.getRoles().add(role);
        userRepository.save(user);
    }

    // ================= REMOVE ROLE =================

    @Override
    public void removeRoleFromUser(RemoveUserRoleRequest request) {

        EUser user = userRepository.findByIdAndIsDeletedFalse(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        ERole role = roleRepository.findByIdAndIsDeletedFalse(request.getRoleId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Role not found"));

        // Prevent removing last role
        if (user.getRoles().size() == 1) {
            throw new BusinessException("User must have at least one role",HttpStatus.BAD_REQUEST);
        }

        user.getRoles().remove(role);
        userRepository.save(user);
    }
}


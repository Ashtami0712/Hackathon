package com.simplogics.base.service;

import com.simplogics.base.entity.Role;
import com.simplogics.base.enums.UserRole;
import com.simplogics.base.exception.FrameworkException;
import com.simplogics.base.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService{

    @Autowired
    RoleRepository rolesRepository;

    @Override
    public Role findByRole(UserRole role) throws FrameworkException {
        return rolesRepository.findByRole(role).orElseThrow(() ->
                new FrameworkException("role.not.found", HttpStatus.BAD_REQUEST));
    }
}

package com.dpc.erp.service;


import com.dpc.erp.entity.Role;
import com.dpc.erp.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;


    public Role getRole(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName);
    }
}

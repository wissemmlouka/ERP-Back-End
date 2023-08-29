package com.dpc.erp.repository;

import com.dpc.erp.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findByRoleNameIgnoreCase(String role);
}

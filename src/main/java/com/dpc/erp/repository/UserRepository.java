package com.dpc.erp.repository;

import com.dpc.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    User findByEmailIgnoreCase(String email);
    Boolean existsByEmailIgnoreCase(String email);

}

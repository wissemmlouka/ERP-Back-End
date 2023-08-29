package com.dpc.erp.repository;

import com.dpc.erp.entity.ConfirmationToken;
import com.dpc.erp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken,Long> {
ConfirmationToken findByConfirmationToken(String token);
ConfirmationToken findByUser(User user);
 boolean existsByConfirmationTokenIgnoreCase(String token);

}

package com.dpc.erp.Request_Response;

import com.dpc.erp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {

    private String token ;
    private UserDetails user;
}

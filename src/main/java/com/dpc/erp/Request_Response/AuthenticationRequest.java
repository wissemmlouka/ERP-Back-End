package com.dpc.erp.Request_Response;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String email;
    private String password;
}

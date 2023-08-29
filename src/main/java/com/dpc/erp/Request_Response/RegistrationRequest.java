package com.dpc.erp.Request_Response;

import lombok.Data;

@Data
public class RegistrationRequest {

    private String username;
    private String email;
    private String role;
}

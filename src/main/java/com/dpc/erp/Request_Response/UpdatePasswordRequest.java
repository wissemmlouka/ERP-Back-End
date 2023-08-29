package com.dpc.erp.Request_Response;

import lombok.Data;

@Data
public class UpdatePasswordRequest {
    private String token;
    private String password;

}

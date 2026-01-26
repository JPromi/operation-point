package com.jpromi.operation_point.model;

import lombok.Data;

@Data
public class AppUserForm {
    private String username;
    private String password;
    private String role;
    private Boolean delete = false;
}

package com.jpromi.operation_point.service;

import com.jpromi.operation_point.entity.AppUser;

public interface UserService {
    void createUser(AppUser user);
    void hashUser(AppUser user);
}

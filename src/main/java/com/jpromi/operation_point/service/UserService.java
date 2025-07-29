package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.AppUser;

public interface UserService {
    AppUser createUser(AppUser user);
    AppUser hashUser(AppUser user);
}

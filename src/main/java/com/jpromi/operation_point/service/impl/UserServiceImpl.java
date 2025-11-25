package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.entity.AppUser;
import com.jpromi.operation_point.repository.AppUserRepository;
import com.jpromi.operation_point.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public AppUser createUser(AppUser user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            throw new IllegalArgumentException("User and its username/password must not be null");
        }
        return appUserRepository.save(user);
    }

    @Override
    public AppUser hashUser(AppUser user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return user;
    }
}

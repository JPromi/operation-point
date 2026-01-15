package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.entity.AppUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsBy();
    Page<AppUser> findByUsernameContainingIgnoreCase(String username, Pageable pageable);
}

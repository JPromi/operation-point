package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.entity.FiredepartmentChange;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FiredepartmentChangeRepository extends JpaRepository<FiredepartmentChange, Long> {
    Optional<FiredepartmentChange> findByUuid(UUID uuid);
    Optional<FiredepartmentChange> findByUuidAndIsVerifiedTrue(UUID uuid);
}

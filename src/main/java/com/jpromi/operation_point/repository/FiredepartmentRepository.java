package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FiredepartmentRepository extends JpaRepository<Firedepartment, Long> {
    Optional<Firedepartment> findByName(String name);
    Optional<Firedepartment> findByUuid(UUID uuid);
    List<Firedepartment> findAllByOrderByNameAsc();
    List<Firedepartment> findByNameContainingIgnoreCase(String name);
    Page<Firedepartment> findByFriendlyNameContainingIgnoreCaseOrderByFriendlyNameAsc(String friednlyName, Pageable pageable);
}

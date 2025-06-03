package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FiredepartmentRepository extends JpaRepository<Firedepartment, Long> {
    Optional<Firedepartment> findByName(String Name);
}

package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.OperationFiredepartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationFiredepartmentRepository extends JpaRepository<OperationFiredepartment, Long> {
    List<OperationFiredepartment> findByFiredepartment(Firedepartment firedepartment);
    void deleteByFiredepartment(Firedepartment firedepartment);
}

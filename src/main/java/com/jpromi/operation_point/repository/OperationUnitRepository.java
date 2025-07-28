package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.OperationFiredepartment;
import com.jpromi.operation_point.enitiy.OperationUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationUnitRepository extends JpaRepository<OperationUnit, Long> {
    List<OperationUnit> findByUnit(Firedepartment firedepartment);
    OperationUnit findByOperationId(Long operationId);
}

package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.OperationFiredepartment;
import com.jpromi.operation_point.enitiy.OperationUnit;
import com.jpromi.operation_point.enitiy.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationUnitRepository extends JpaRepository<OperationUnit, Long> {
    List<OperationUnit> findByUnit(Unit unit);
    OperationUnit findByOperationId(Long operationId);
    void deleteByUnit(Unit unit);
}

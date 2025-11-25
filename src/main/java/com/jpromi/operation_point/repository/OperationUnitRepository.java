package com.jpromi.operation_point.repository;

import com.jpromi.operation_point.entity.OperationUnit;
import com.jpromi.operation_point.entity.Unit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OperationUnitRepository extends JpaRepository<OperationUnit, Long> {
    List<OperationUnit> findByUnit(Unit unit);
    OperationUnit findByOperationId(Long operationId);
    void deleteByUnit(Unit unit);
}

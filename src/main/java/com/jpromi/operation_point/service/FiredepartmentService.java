package com.jpromi.operation_point.service;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.entity.Unit;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.repository.OperationRepository;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FiredepartmentService {
    List<Firedepartment> getList();
    Page<Firedepartment> getList(String query, Integer limit, Integer page);
    Firedepartment getByUuid(UUID uuid);
    Firedepartment getByNameId(String nameId);
    Unit assignAsUnit(Firedepartment firedepartment);
    List<Operation> getActiveOperations(UUID firedepartmentUuid);
}

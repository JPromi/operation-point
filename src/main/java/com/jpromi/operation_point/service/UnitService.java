package com.jpromi.operation_point.service;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.Unit;

import java.util.List;
import java.util.UUID;

public interface UnitService {
    List<Unit> getList();
    List<Unit> getList(String query);
    Unit getByUuid(UUID uuid);
    Firedepartment assignAsFiredepartment(Unit unit);
}

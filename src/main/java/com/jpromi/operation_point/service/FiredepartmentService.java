package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.Firedepartment;

import java.util.List;
import java.util.UUID;

public interface FiredepartmentService {
    List<Firedepartment> getList();
    List<Firedepartment> getList(String query);
    Firedepartment getByUuid(UUID uuid);
}

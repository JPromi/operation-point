package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Unit;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface FiredepartmentService {
    List<Firedepartment> getList();
    Page<Firedepartment> getList(String query, Integer limit, Integer page);
    Firedepartment getByUuid(UUID uuid);
    Unit assignAsUnit(Firedepartment firedepartment);
}

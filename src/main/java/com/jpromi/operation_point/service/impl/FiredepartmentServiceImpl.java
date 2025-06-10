package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FiredepartmentServiceImpl implements FiredepartmentService {

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @Override
    public List<Firedepartment> getList() {
        return firedepartmentRepository.findAll();
    }

    @Override
    public List<Firedepartment> getList(String query) {
        if (query == null || query.isEmpty()) {
            return firedepartmentRepository.findAll();
        }
        return firedepartmentRepository.findByNameContainingIgnoreCase(query);
    }

    @Override
    public Firedepartment getByUuid(UUID uuid) {
        return firedepartmentRepository.findByUuid(uuid)
                .orElse(null);
    }
}

package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.enitiy.Unit;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Override
    public List<Unit> getList() {
        return getList(null);
    }

    @Override
    public List<Unit> getList(String query) {
        if (query == null || query.isEmpty()) {
            return unitRepository.findAll();
        } else {
            return unitRepository.findByFriendlyNameContainingIgnoreCase(query);
        }
    }

    @Override
    public Unit getByUuid(UUID uuid) {
        return unitRepository.findByUuid(uuid).orElse(null);
    }

}

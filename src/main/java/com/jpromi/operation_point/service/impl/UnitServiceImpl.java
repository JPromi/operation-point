package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.OperationFiredepartment;
import com.jpromi.operation_point.enitiy.OperationUnit;
import com.jpromi.operation_point.enitiy.Unit;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationFiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationUnitRepository;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UnitServiceImpl implements UnitService {

    @Autowired
    private UnitRepository unitRepository;

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @Autowired
    private OperationUnitRepository operationUnitRepository;

    @Autowired
    private OperationFiredepartmentRepository operationFiredepartmentRepository;

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

    @Override
    @Transactional
    public Firedepartment assignAsFiredepartment(Unit unit) {
        // new firedepartment
        Firedepartment firedepartment = Firedepartment.builder()
                .friendlyName(unit.getFriendlyName())
                .name(unit.getName())
                .build();

        // save firedepartment
        Firedepartment firedepartmentSaved = firedepartmentRepository.save(firedepartment);

        // get all
        operationUnitRepository.findByUnit(unit)
                .forEach(operationUnit -> {
                    OperationFiredepartment operationFiredepartment = OperationFiredepartment.builder()
                            .firedepartment(firedepartmentSaved)
                            .operation(operationUnit.getOperation())
                            .outTime(operationUnit.getOutTime())
                            .inTime(operationUnit.getInTime())
                            .alarmTime(operationUnit.getAlarmTime())
                            .dispoTime(operationUnit.getDispoTime())
                            .build();

                    // save operation unit
                    operationFiredepartmentRepository.save(operationFiredepartment);
                });

        // delete unit
        operationUnitRepository.deleteByUnit(unit);
        unitRepository.delete(unit);

        return firedepartmentSaved;
    }

}

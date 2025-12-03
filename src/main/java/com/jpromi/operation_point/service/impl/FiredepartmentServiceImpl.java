package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.OperationUnit;
import com.jpromi.operation_point.entity.Unit;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationFiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationUnitRepository;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class FiredepartmentServiceImpl implements FiredepartmentService {
    private final FiredepartmentRepository firedepartmentRepository;
    private final UnitRepository unitRepository;
    private final OperationFiredepartmentRepository operationFiredepartmentRepository;
    private final OperationUnitRepository operationUnitRepository;

    @Autowired
    public FiredepartmentServiceImpl(FiredepartmentRepository firedepartmentRepository, UnitRepository unitRepository, OperationFiredepartmentRepository operationFiredepartmentRepository, OperationUnitRepository operationUnitRepository) {
        this.firedepartmentRepository = firedepartmentRepository;
        this.unitRepository = unitRepository;
        this.operationFiredepartmentRepository = operationFiredepartmentRepository;
        this.operationUnitRepository = operationUnitRepository;
    }

    @Override
    public List<Firedepartment> getList() {
        return firedepartmentRepository.findAll();
    }

    @Override
    public Page<Firedepartment> getList(String query, Integer limit, Integer page) {
        Pageable pageable = PageRequest.of(page, limit);
        return firedepartmentRepository.findByFriendlyNameContainingIgnoreCaseAndIsHiddenIsFalseOrderByFriendlyNameAsc(query, pageable);
    }

    @Override
    public Firedepartment getByUuid(UUID uuid) {
        return firedepartmentRepository.findByUuid(uuid)
                .orElse(null);
    }

    @Override
    @Transactional
    public Unit assignAsUnit(Firedepartment firedepartment) {
        // new unit
        Unit unit = Unit.builder()
                .friendlyName(firedepartment.getFriendlyName())
                .name(firedepartment.getName())
                .build();

        // save unit
        Unit unitSaved = unitRepository.save(unit);

        // get all
        operationFiredepartmentRepository.findByFiredepartment(firedepartment)
                .forEach(operationFiredepartment -> {
                    OperationUnit operationUnit = OperationUnit.builder()
                            .unit(unitSaved)
                            .operation(operationFiredepartment.getOperation())
                            .outTime(operationFiredepartment.getOutTime())
                            .inTime(operationFiredepartment.getInTime())
                            .alarmTime(operationFiredepartment.getAlarmTime())
                            .dispoTime(operationFiredepartment.getDispoTime())
                            .build();

                    // save operation unit
                    operationUnitRepository.save(operationUnit);
                });

        // delete firedepartment
        operationFiredepartmentRepository.deleteByFiredepartment(firedepartment);
        firedepartmentRepository.delete(firedepartment);

        return unitSaved;
    }
}

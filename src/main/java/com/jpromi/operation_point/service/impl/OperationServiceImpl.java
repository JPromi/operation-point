package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.LocationService;
import com.jpromi.operation_point.service.OperationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private OperationVariableService operationVariableService;

    @Autowired
    private LocationService locationService;

    @Override
    public List<Operation> getActiveOperations() {
        return operationRepository.findByEndTimeNullOrderByStartTime();
    }

    @Override
    public List<Operation> getActiveOperationsByFederalState(String federalState) {
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        federalState = operationVariableService.getFederalState(federalState);
        return operationRepository.findByEndTimeNullAndFederalStateOrderByStartTime(federalState);
    }

    @Override
    public Long getActiveOperationCount() {
        return operationRepository.countByEndTimeNullOrderByStartTime();
    }

    @Override
    public Long getActiveOperationsByFederalStateCount(String federalState) {
        return operationRepository.countByEndTimeNullAndFederalStateOrderByStartTime(operationVariableService.getFederalState(federalState));
    }

    @Override
    public Operation getOperationByUuid(UUID uuid) {
        Optional<Operation> operation = operationRepository.findByUuid(uuid);
        return operation.orElse(null);
    }

    @Override
    public List<Operation> getActiveOperationsByFederalStateAndDistrict(String federalState, String district) {
        // search district
        district = locationService.getDistrictByDistrictId(district);
        if (district == null) {
            throw new IllegalArgumentException("District not found");
        }
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        federalState = operationVariableService.getFederalState(federalState);
        return operationRepository.findByEndTimeNullAndFederalStateAndDistrictIgnoreCaseOrderByStartTime(federalState, district);
    }
}

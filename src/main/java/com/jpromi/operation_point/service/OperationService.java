package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.Operation;

import java.util.List;
import java.util.UUID;

public interface OperationService {
    public List<Operation> getActiveOperations();
    public List<Operation> getActiveOperationsByFederalState(String federalState);
    public List<Operation> getActiveOperationsByFederalStateAndDistrict(String federalState, String district);
    public Long getActiveOperationCount();
    public Long getActiveOperationsByFederalStateCount(String federalState);
    public Operation getOperationByUuid(UUID uuid);
}

package com.jpromi.operation_point.service;

import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.mapper.OperationResponseMapper;
import com.jpromi.operation_point.model.OperationResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public interface OperationService {
    List<Operation> getActiveOperations();
    List<Operation> getActiveOperationsByFederalState(String federalState);
    List<Operation> getActiveOperationsByFederalStateAndDistrict(String federalState, String district);
    Long getActiveOperationCount();
    Long getActiveOperationsByFederalStateCount(String federalState);
    Operation getOperationByUuid(UUID uuid);


    @NonNull
    static ResponseEntity<List<OperationResponse>> BuildResponseFromOperations(List<Operation> operations, OperationResponseMapper operationResponseMapper) {
        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        operationResponses.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
        return ResponseEntity.ok(operationResponses);
    }
}

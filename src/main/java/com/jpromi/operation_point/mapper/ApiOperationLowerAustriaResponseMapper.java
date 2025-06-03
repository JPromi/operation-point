package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.model.ApiOperationLowerAustriaResponse;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;

public class ApiOperationLowerAustriaResponseMapper {

    @Autowired
    private OperationVariableService operationVariableService;

    public Operation toOperation(ApiOperationLowerAustriaResponse laOperation, String laWastlPubId) {
        Operation operation = Operation.builder()
                .laWastlPubId(laWastlPubId)
                .laId(laOperation.getId())
                .laSysId(laOperation.getN())

                .alarmLevel(operationVariableService.getAlarmLevel(laOperation.getA()))
                .alarmType(operationVariableService.getAlarmType(laOperation.getA()))
                .alarmText(laOperation.getM())
                .startTime(operationVariableService.getTimeFromDateAndTime(laOperation.getD(), laOperation.getT()))
                .zipCode(laOperation.getP())
                .city(laOperation.getO())
                .build();

        return operation;
    }
}

package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.mapper.OperationResponseMapper;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController("OperationController")
@RequestMapping("/operation")
public class OperationController {
    /*
    Features:
    GET - list (search by: bundesland, city, zip code,...)
    GET - details
    */

    @Autowired
    private OperationService operationService;

    @Autowired
    private OperationResponseMapper operationResponseMapper;

    @GetMapping(value = "list", produces = {"application/json"})
    public ResponseEntity<List<OperationResponse>> getList() {
        List<Operation> operations = operationService.getActiveOperations();
        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        return ResponseEntity.ok(operationResponses);
    }

    @GetMapping(value = "list/count", produces = {"application/json"})
    public ResponseEntity<Long> getCount() {
        Long count = operationService.getActiveOperationCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "list/{federalState}", produces = {"application/json"})
    public ResponseEntity<List<OperationResponse>> getListByFederalState(@PathVariable String federalState) {
        List<Operation> operations = operationService.getActiveOperationsByFederalState(federalState);
        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        return ResponseEntity.ok(operationResponses);
    }

    @GetMapping(value = "list/{federalState}/count", produces = {"application/json"})
    public ResponseEntity<Long> getCountByFederalState(@PathVariable String federalState) {
        Long count = operationService.getActiveOperationsByFederalStateCount(federalState);
        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "{uuid}", produces = {"application/json"})
    public ResponseEntity<OperationResponse> getOperationByUuid(@PathVariable String uuid) {
        Operation operation = operationService.getOperationByUuid(UUID.fromString(uuid));
        if (operation == null) {
            return ResponseEntity.notFound().build();
        } else {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            return ResponseEntity.ok(response);
        }
    }
}

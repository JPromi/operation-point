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

    @GetMapping("")
    public ResponseEntity<List<OperationResponse>> getList() {
        List<Operation> operations = operationService.getActiveOperations();
        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        return ResponseEntity.ok(operationResponses);
    }

    @GetMapping("/{federalState}")
    public ResponseEntity<List<OperationResponse>> getListByFederalState(@PathVariable String federalState) {
        List<Operation> operations = operationService.getActiveOperationsByFederalState(federalState);
        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        return ResponseEntity.ok(operationResponses);
    }
}

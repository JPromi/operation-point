package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.mapper.LocationStatisticResponseMapper;
import com.jpromi.operation_point.mapper.OperationResponseMapper;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    private final OperationService operationService;
    private final OperationResponseMapper operationResponseMapper;
    private final LocationStatisticResponseMapper locationStatisticResponseMapper;

    @Autowired
    public OperationController(OperationService operationService, OperationResponseMapper operationResponseMapper, LocationStatisticResponseMapper locationStatisticResponseMapper) {
        this.operationService = operationService;
        this.operationResponseMapper = operationResponseMapper;
        this.locationStatisticResponseMapper = locationStatisticResponseMapper;
    }

    @GetMapping(value = "list", produces = {"application/json"})
    public ResponseEntity<List<OperationResponse>> getList() {
        List<Operation> operations = operationService.getActiveOperations();
        return OperationService.BuildResponseFromOperations(operations, operationResponseMapper);
    }

    @GetMapping(value = "list/count", produces = {"application/json"})
    public ResponseEntity<Long> getCount() {
        Long count = operationService.getActiveOperationCount();
        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "list/statistic", produces = {"application/json"})
    public ResponseEntity<List<LocationStatisticResponse>> getStatistic() {
        List<Operation> operations = operationService.getActiveOperations();
        List<LocationStatisticResponse> statisticResponses = new ArrayList<>();

        if (operations != null && !operations.isEmpty()) {
            statisticResponses = locationStatisticResponseMapper.fromOperationFederalState(operations);
        }

        return ResponseEntity.ok(statisticResponses);
    }

    @GetMapping(value = "list/{federalState}", produces = {"application/json"})
    public ResponseEntity<List<OperationResponse>> getListByFederalState(@PathVariable String federalState, @RequestParam(required = false) String district) {
        List<Operation> operations;

        if(district != null) {
            operations = operationService.getActiveOperationsByFederalStateAndDistrict(federalState, district);
        } else {
            operations = operationService.getActiveOperationsByFederalState(federalState);
        }
        return OperationService.BuildResponseFromOperations(operations, operationResponseMapper);
    }

    @GetMapping(value = "list/{federalState}/count", produces = {"application/json"})
    public ResponseEntity<Long> getCountByFederalState(@PathVariable String federalState) {
        Long count = operationService.getActiveOperationsByFederalStateCount(federalState);
        return ResponseEntity.ok(count);
    }

    @GetMapping(value = "list/{federalState}/statistic", produces = {"application/json"})
    public ResponseEntity<List<LocationStatisticResponse>> getStatisticByFederalState(@PathVariable String federalState) {
        List<Operation> operations = operationService.getActiveOperationsByFederalState(federalState);
        List<LocationStatisticResponse> statisticResponses = new ArrayList<>();
        if (operations != null && !operations.isEmpty()) {
            statisticResponses = locationStatisticResponseMapper.fromOperationDistrict(operations, federalState);
        }
        return ResponseEntity.ok(statisticResponses);
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

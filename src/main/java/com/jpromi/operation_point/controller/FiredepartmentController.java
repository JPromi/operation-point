package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.entity.Firedepartment;
import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.mapper.FiredepartmentResponseMapper;
import com.jpromi.operation_point.mapper.OperationResponseMapper;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.FiredepartmentService;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController("FiredepartmentController")
@RequestMapping("/firedepartment")
public class FiredepartmentController {
    /*
    Features:
    GET - list (search by: name)
    GET - details
    */

    private final FiredepartmentService firedepartmentService;
    private final FiredepartmentResponseMapper firedepartmentResponseMapper;
    private final OperationResponseMapper operationResponseMapper;
    private final OperationRepository operationRepository;

    @Autowired
    public FiredepartmentController(FiredepartmentService firedepartmentService, FiredepartmentResponseMapper firedepartmentResponseMapper, OperationResponseMapper operationResponseMapper, OperationRepository operationRepository) {
        this.firedepartmentService = firedepartmentService;
        this.firedepartmentResponseMapper = firedepartmentResponseMapper;
        this.operationResponseMapper = operationResponseMapper;
        this.operationRepository = operationRepository;
    }

    @GetMapping(value = "list", produces = {"application/json"})
    public ResponseEntity<Page<FiredepartmentResponse>> getList(
            @RequestParam(required = false, value = "q") String query,
            @RequestParam(required = false, value = "limit", defaultValue = "20") Integer limit,
            @RequestParam(required = false, value = "page", defaultValue = "0") Integer page) {
        Page<Firedepartment> result = firedepartmentService.getList(query, limit, page);
        Page<FiredepartmentResponse> dto = result.map(firedepartmentResponseMapper::fromFiredepartment);
        return ResponseEntity.ok(dto);
    }

    @GetMapping(value = "{uuid}", produces = {"application/json"})
    public ResponseEntity<FiredepartmentResponse> getFiredepartmentByUuid(@PathVariable String uuid) {
        Firedepartment firedepartment;
        if (isNameId(uuid)) {
            firedepartment = firedepartmentService.getByNameId(uuid);
        } else {
            firedepartment = firedepartmentService.getByUuid(UUID.fromString(uuid));
        }
        if (firedepartment == null) {
            return ResponseEntity.notFound().build();
        }
        FiredepartmentResponse response = firedepartmentResponseMapper.fromFiredepartment(firedepartment);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "{uuid}/active-operations", produces = {"application/json"})
    public ResponseEntity<List<OperationResponse>> getActiveOperationsByFiredepartmentUuid(@PathVariable String uuid) {
        List<Operation> operations;
        if (isNameId(uuid)) {
            Firedepartment firedepartment = firedepartmentService.getByNameId(uuid);
            operations = firedepartmentService.getActiveOperations(firedepartment.getUuid());
        } else {
            operations = firedepartmentService.getActiveOperations(UUID.fromString(uuid));
        }

        List<OperationResponse> operationResponses = new ArrayList<>();
        for (Operation operation : operations) {
            OperationResponse response = operationResponseMapper.fromOperation(operation);
            operationResponses.add(response);
        }
        operationResponses.sort((o1, o2) -> o2.getStartTime().compareTo(o1.getStartTime()));
        return ResponseEntity.ok(operationResponses);
    }

    @GetMapping(value = "{uuid}/operations", produces = {"application/json"})
    public ResponseEntity<Page<OperationResponse>> getAllOperationsByFiredepartmentUuid(
            @PathVariable String uuid,
            @RequestParam(required = false) Instant dateStart,
            @RequestParam(required = false) Instant dateEnd,
            Pageable pageable) {
        UUID firedepartmentUuid;
        if (isNameId(uuid)) {
            Firedepartment firedepartment = firedepartmentService.getByNameId(uuid);
            firedepartmentUuid = firedepartment.getUuid();
        } else {
            firedepartmentUuid = UUID.fromString(uuid);
        }
        Page<Operation> operations = operationRepository.findByFiredepartmentFiltered(firedepartmentUuid, dateStart, dateEnd, pageable);
        Page<OperationResponse> dto = operations.map(operationResponseMapper::fromOperation);
        return ResponseEntity.ok(dto);
    }

    private Boolean isNameId(String query) {
        try {
            UUID.fromString(query);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}

package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Unit;
import com.jpromi.operation_point.mapper.FiredepartmentResponseMapper;
import com.jpromi.operation_point.mapper.UnitResponseMapper;
import com.jpromi.operation_point.model.FiredepartmentResponse;
import com.jpromi.operation_point.model.UnitResponse;
import com.jpromi.operation_point.service.FiredepartmentService;
import com.jpromi.operation_point.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    public FiredepartmentController(FiredepartmentService firedepartmentService, FiredepartmentResponseMapper firedepartmentResponseMapper) {
        this.firedepartmentService = firedepartmentService;
        this.firedepartmentResponseMapper = firedepartmentResponseMapper;
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
        Firedepartment firedepartment = firedepartmentService.getByUuid(UUID.fromString(uuid));
        if (firedepartment == null) {
            return ResponseEntity.notFound().build();
        }
        FiredepartmentResponse response = firedepartmentResponseMapper.fromFiredepartment(firedepartment);
        return ResponseEntity.ok(response);
    }
}

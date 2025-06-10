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

    @Autowired
    private FiredepartmentService firedepartmentService;

    @Autowired
    private FiredepartmentResponseMapper firedepartmentResponseMapper;

    @GetMapping(value = "list", produces = {"application/json"})
    public ResponseEntity<List<FiredepartmentResponse>> getList(@RequestParam(required = false, value = "q") String query) {
        List<Firedepartment> firedepartments = firedepartmentService.getList(query);
        return ResponseEntity.ok(
                firedepartments.stream()
                        .map(firedepartmentResponseMapper::fromFiredepartment)
                        .toList()
        );
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

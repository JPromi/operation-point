package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.entity.Unit;
import com.jpromi.operation_point.mapper.UnitResponseMapper;
import com.jpromi.operation_point.model.UnitResponse;
import com.jpromi.operation_point.service.UnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController("UnitController")
@RequestMapping("/unit")
public class UnitController {
    /*
    Features:
    GET - list (search by: name)
    GET - details
    */

    private final UnitService unitService;
    private final UnitResponseMapper unitResponseMapper;

    @Autowired
    public UnitController(UnitService unitService, UnitResponseMapper unitResponseMapper) {
        this.unitService = unitService;
        this.unitResponseMapper = unitResponseMapper;
    }

    @GetMapping(value = "list", produces = {"application/json"})
    public ResponseEntity<List<UnitResponse>> getList(@RequestParam(required = false, value = "q") String query) {
        List<Unit> units = unitService.getList(query);
        return ResponseEntity.ok(
            units.stream()
                .map(unitResponseMapper::fromUnit)
                .toList()
        );
    }

    @GetMapping(value = "{uuid}", produces = {"application/json"})
    public ResponseEntity<UnitResponse> getUnitByUuid(@PathVariable String uuid) {
        Unit unit = unitService.getByUuid(UUID.fromString(uuid));
        if (unit == null) {
            return ResponseEntity.notFound().build();
        }
        UnitResponse response = unitResponseMapper.fromUnit(unit);
        return ResponseEntity.ok(response);
    }
}

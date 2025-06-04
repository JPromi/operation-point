package com.jpromi.operation_point.controller;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("")
    public ResponseEntity<List<Operation>> searchList() {
        return ResponseEntity.ok(operationService.getActiveOperations());
    }
}

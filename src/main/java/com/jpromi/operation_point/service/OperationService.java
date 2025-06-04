package com.jpromi.operation_point.service;

import com.jpromi.operation_point.enitiy.Operation;

import java.util.List;

public interface OperationService {
    public List<Operation> getActiveOperations();
}

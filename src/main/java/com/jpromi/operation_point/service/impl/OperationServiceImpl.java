package com.jpromi.operation_point.service.impl;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Override
    public List<Operation> getActiveOperations() {
        return operationRepository.findByEndTimeNull();
    }
}

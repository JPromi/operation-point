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
        return operationRepository.findByEndTimeNullOrderByStartTime();
    }

    @Override
    public List<Operation> getActiveOperationsByFederalState(String federalState) {
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        switch (federalState) {
            case "ua", "upper-austria":
                federalState = "Upper Austria";
                break;
            case "st", "styria":
                federalState = "Styria";
                break;
            case "ty", "tyrol":
                federalState = "tyrol";
                break;
            case "la", "lower-austria":
                federalState = "Lower Austria";
                break;
            case "bg", "burgenland", "bl":
                federalState = "Burgenland";
                break;
        }
        return operationRepository.findByEndTimeNullAndFederalStateOrderByStartTime(federalState);
    }
}

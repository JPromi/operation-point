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
        federalState = getFederalState(federalState);
        return operationRepository.findByEndTimeNullAndFederalStateOrderByStartTime(federalState);
    }

    @Override
    public Long getActiveOperationCount() {
        return operationRepository.countByEndTimeNullOrderByStartTime();
    }

    @Override
    public Long getActiveOperationsByFederalStateCount(String federalState) {
        return operationRepository.countByEndTimeNullAndFederalStateOrderByStartTime(getFederalState(federalState));
    }

    private String getFederalState(String federalState) {
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        switch (federalState) {
            case "ua", "upper-austria":
                return "Upper Austria";
            case "st", "styria":
                return "Styria";
            case "ty", "tyrol":
                return "Tyrol";
            case "la", "lower-austria":
                return "Lower Austria";
            case "bg", "burgenland", "bl":
                return "Burgenland";
            default:
                return federalState;
        }
    }
}

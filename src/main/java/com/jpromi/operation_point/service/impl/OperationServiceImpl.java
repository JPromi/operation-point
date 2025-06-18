package com.jpromi.operation_point.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public Operation getOperationByUuid(UUID uuid) {
        Optional<Operation> operation = operationRepository.findByUuid(uuid);
        return operation.orElse(null);
    }

    @Override
    public List<Operation> getActiveOperationsByFederalStateAndDistrict(String federalState, String district) {
        // search district
        district = getDistrict(district);
        if (district == null) {
            throw new IllegalArgumentException("District not found");
        }
        federalState = federalState.toLowerCase();
        federalState = federalState.replaceAll(" ", "-");
        federalState = getFederalState(federalState);
        return operationRepository.findByEndTimeNullAndFederalStateAndDistrictIgnoreCaseOrderByStartTime(federalState, district);
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

    private String getDistrict(String districtId) {
        if(districtId == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("mapping/district-mapping.json")) {
            if (is == null) {
                throw new IllegalStateException("File not found: mapping/district-mapping.json");
            }

            // list of districts
            List<Map<String, String>> districtList = mapper.readValue(is, new TypeReference<List<Map<String, String>>>() {});

            // search for the district by ID
            for (Map<String, String> entry : districtList) {
                if (districtId.equals(entry.get("id"))) {
                    return entry.get("name");
                }
            }

            throw new IllegalArgumentException("District ID not found: " + districtId);
        } catch (IOException e) {
            throw new RuntimeException("Error reading district-mapping.json", e);
        }
    }
}

package com.jpromi.operation_point.service.impl;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpromi.operation_point.ServiceOriginEnum;
import com.jpromi.operation_point.enitiy.Firedepartment;
import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.enitiy.OperationFiredepartment;
import com.jpromi.operation_point.enitiy.OperationUnit;
import com.jpromi.operation_point.model.ApiOperationBurgenlandResponse;
import com.jpromi.operation_point.model.ApiOperationUpperAustriaResponse;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.service.ApiOperationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ApiOperationServiceImpl implements ApiOperationService {

    @Autowired
    private OperationVariableService operationVariableService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @Override
    public List<Operation> getOperationListBurgenland() {
        try {
            // get data from API
            WebClient webClient = WebClient.create();
            ApiOperationBurgenlandResponse result = webClient.get()
                    .uri("https://www.lsz-b.at/fileadmin/fw_apps/api/")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ApiOperationBurgenlandResponse.class)
                    .block();

            // build operation
            List<Operation> operationList = new ArrayList<>();

            result.getOperations_current().forEach( operation -> {
                Operation _operation = updateSavedOperationBurgenland(operation);
                if (_operation != null) {
                    operationList.add(_operation);
                }
            });

            checkVanishedOperations(operationList, ServiceOriginEnum.BL_LSZ_PUB);

            return operationList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching operations from Burgenland API", e);
        }
    }

    @Override
    public List<Operation> getOperationListLowerAustria() {
        return null;
    }

    @Override
    public List<Operation> getOperationListUpperAustria() {
        try {
            // get data from API
            WebClient webClient = WebClient.create();
            String json = webClient.get()
                    .uri("https://cf-einsaetze.ooelfv.at/webext2/rss/json_laufend.txt")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            ApiOperationUpperAustriaResponse result = objectMapper.readValue(json, ApiOperationUpperAustriaResponse.class);

            // build operation
            List<Operation> operationList = new ArrayList<>();

            for (ApiOperationUpperAustriaResponse.ApiOperationUpperAustriaResponseOperationWrapper wrapper : result.getEinsaetze().values()) {
                ApiOperationUpperAustriaResponse.ApiOperationUpperAustriaResponseOperation operation = wrapper.getEinsatz();
                operationList.add(updateSavedOperationUpperAustria(operation));
            }

            checkVanishedOperations(operationList, ServiceOriginEnum.UA_LFV_PUB);

            return operationList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching operations from Burgenland API", e);
        }
    }

    @Override
    public List<Operation> getOperationListStyria() {
        return null;
    }

    @Override
    public List<Operation> getOperationListTyrol() {
        return null;
    }

    private Operation updateSavedOperationBurgenland(ApiOperationBurgenlandResponse.ApiOperationBurgenlandResponseOperation response) {
        if (response.getInfo() == null || !response.getInfo()) {
            Optional<Operation> _operation = operationRepository.findByBlId(response.getOperationId());

            if (_operation.isPresent()) {
                Operation operation = _operation.get();
                operation.setAlarmLevel(operationVariableService.getAlarmLevel(response.getCode()));
                operation.setAlarmType(operationVariableService.getAlarmType(response.getCode()));
                operation.setAlarmText(response.getCodeDesc());

                // units
                int unitCount = Integer.parseInt(response.getNumVehicles());
                // only add units if they are not already present
                List<OperationUnit> operationUnits = operation.getUnits();
                if (operationUnits == null) {
                    operationUnits = new ArrayList<>();
                }
                for (int i = operationUnits.size(); i < unitCount; i++) {
                    OperationUnit unit = OperationUnit.builder()
                            .operation(operation)
                            .build();
                    operationUnits.add(unit);
                }
                operation.setUnits(operationUnits);

                // firedepartments
                List<OperationFiredepartment> firedepartments = List.of();
                if (firedepartments == null) {
                    firedepartments = new ArrayList<>();
                } else {
                    firedepartments = operation.getFiredepartments();
                }

                List<OperationFiredepartment> finalFiredepartments = firedepartments;
                response.getFwLocations().forEach(fwName -> {
                    Firedepartment firedepartment = createFiredepartmentIfNotExists(
                            Firedepartment.builder().name(fwName).build()
                    );

                    // check if firedepartment already exists
                    boolean exists = finalFiredepartments.stream()
                            .anyMatch(opFd -> opFd.getFiredepartment().getName().equals(firedepartment.getName()));
                    if (!exists) {
                        OperationFiredepartment opFd = OperationFiredepartment.builder()
                                .firedepartment(firedepartment)
                                .operation(operation)
                                .build();
                        finalFiredepartments.add(opFd);
                    }
                });
                operation.setFiredepartments(finalFiredepartments);
                operation.setUpdatedAt(OffsetDateTime.now());

                return operationRepository.save(operation);
            } else {
                long operationStart = Long.parseLong(response.getStart());

                Operation operation = Operation.builder()
                        .blId(response.getOperationId())
                        .alarmLevel(operationVariableService.getAlarmLevel(response.getCode()))
                        .alarmType(operationVariableService.getAlarmType(response.getCode()))
                        .alarmText(response.getCodeDesc())
                        .startTime(Instant.ofEpochSecond(operationStart).atOffset(OffsetDateTime.now().getOffset()))
                        .location(response.getPlaceOfOperation())
                        .federalState("Burgenland")
                        .serviceOrigin(ServiceOriginEnum.BL_LSZ_PUB)
                        .build();

                // firedepartments
                List<OperationFiredepartment> firedepartments = new ArrayList<>();
                response.getFwLocations().forEach(fwName -> {
                    Firedepartment firedepartment = createFiredepartmentIfNotExists(
                            Firedepartment.builder().name(fwName).build()
                    );

                    OperationFiredepartment opFd = OperationFiredepartment.builder()
                            .firedepartment(firedepartment)
                            .operation(operation)
                            .build();

                    firedepartments.add(opFd);
                });
                operation.setFiredepartments(firedepartments);

                // units
                int unitCount = Integer.parseInt(response.getNumVehicles());
                List<OperationUnit> operationUnits = new ArrayList<>();
                for (int i = 0; i < unitCount; i++) {
                    OperationUnit unit = OperationUnit.builder()
                            .operation(operation)
                            .build();
                    operationUnits.add(unit);
                }
                operation.setUnits(operationUnits);

                return operationRepository.save(operation);
            }
        }

        return null;
    }

    private Operation updateSavedOperationUpperAustria(ApiOperationUpperAustriaResponse.ApiOperationUpperAustriaResponseOperation response) {
        Optional<Operation> _operation = operationRepository.findByUaId(response.getNum1());

        if(_operation.isPresent()) {
            Operation operation = _operation.get();
            operation.setAlarmLevel(Long.parseLong(response.getAlarmstufe()));
            operation.setUaAlarmTypeType(response.getEinsatztyp().getId());
            operation.setUaAlarmTypeId(response.getEinsatztyp().getId());
            operation.setAlarmText(response.getEinsatztyp().getText());
            operation.setLocation(response.getAdresse().getEarea());
            operation.setCity(response.getAdresse().getEarea());

            operation.setUpdatedAt(OffsetDateTime.now());

            // firedepartments
            List<OperationFiredepartment> firedepartments = operation.getFiredepartments();
            List<OperationFiredepartment> finalFiredepartments = firedepartments;
            for (ApiOperationUpperAustriaResponse.ApiOperationUpperAustriaResponseOperation.ApiOperationUpperAustriaResponseOperationFeuerwehrArray firedepartment : response.getFeuerwehrenarray().values()) {
                Firedepartment _firedepartment = createFiredepartmentIfNotExists(
                        Firedepartment.builder()
                                .name(firedepartment.getFwname())
                                .atFireDepartmentId(firedepartment.getFwnr().toString())
                                .build()
                );

                // check if firedepartment already exists
                boolean exists = finalFiredepartments.stream()
                        .anyMatch(opFd -> opFd.getFiredepartment().getName().equals(firedepartment.getFwname()));
                if (!exists) {
                    OperationFiredepartment opFd = OperationFiredepartment.builder()
                            .firedepartment(_firedepartment)
                            .operation(operation)
                            .build();
                    firedepartments.add(opFd);
                }
            }
            operation.setFiredepartments(firedepartments);

            return operationRepository.save(operation);
        } else {
            Operation operation = Operation.builder()
                    .uaId(response.getNum1())
                    .alarmLevel(Long.parseLong(response.getAlarmstufe()))
                    .uaAlarmTypeType(response.getEinsatztyp().getId())
                    .uaAlarmTypeId(response.getEinsatztyp().getId())
                    .alarmText(response.getEinsatztyp().getText())
                    .location(response.getAdresse().getEarea())
                    .city(response.getAdresse().getEarea())
                    .federalState("Upper Austria")
                    .serviceOrigin(ServiceOriginEnum.UA_LFV_PUB)
                    .startTime(OffsetDateTime.parse(response.getStartzeit(), DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z")))
                    .build();

            // firedepartments
            List<OperationFiredepartment> firedepartments = new ArrayList<>();
            for (ApiOperationUpperAustriaResponse.ApiOperationUpperAustriaResponseOperation.ApiOperationUpperAustriaResponseOperationFeuerwehrArray firedepartment : response.getFeuerwehrenarray().values()) {
                Firedepartment _firedepartment = createFiredepartmentIfNotExists(
                        Firedepartment.builder()
                                .name(firedepartment.getFwname())
                                .atFireDepartmentId(firedepartment.getFwnr().toString())
                                .build()
                );

                // check if firedepartment already exists
                boolean exists = firedepartments.stream()
                        .anyMatch(opFd -> opFd.getFiredepartment().getAtFireDepartmentId().equals(_firedepartment.getAtFireDepartmentId()));
                if (!exists) {
                    OperationFiredepartment opFd = OperationFiredepartment.builder()
                            .firedepartment(_firedepartment)
                            .operation(operation)
                            .build();
                    firedepartments.add(opFd);
                }
            }

            operation.setFiredepartments(firedepartments);

            return operationRepository.save(operation);
        }
    }


    private Firedepartment createFiredepartmentIfNotExists(Firedepartment firedepartment) {
        Optional<Firedepartment> existing = firedepartmentRepository.findByName(firedepartment.getName());
        return existing.orElseGet(() -> firedepartmentRepository.save(firedepartment));
    }

    private void checkVanishedOperations(List<Operation> operations, ServiceOriginEnum service) {
        List<Operation> savedOperations = operationRepository.findByServiceOriginAndLastSeenNull(service);
        if (savedOperations != null && !savedOperations.isEmpty()) {
            for (Operation savedOperation : savedOperations) {
                boolean found = operations.stream()
                        .anyMatch(op -> op.getId() != null && op.getId().equals(savedOperation.getId()));
                if (!found) {
                    savedOperation.setLastSeen(OffsetDateTime.now());
                    if(savedOperation.getEndTime() == null) {
                        savedOperation.setEndTime(OffsetDateTime.now());
                    }
                    operationRepository.save(savedOperation);
                }
            }
        }
    }

}

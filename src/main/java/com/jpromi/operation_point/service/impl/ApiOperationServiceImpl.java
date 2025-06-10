package com.jpromi.operation_point.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpromi.operation_point.enums.ServiceOriginEnum;
import com.jpromi.operation_point.enitiy.*;
import com.jpromi.operation_point.model.*;
import com.jpromi.operation_point.repository.FiredepartmentRepository;
import com.jpromi.operation_point.repository.OperationRepository;
import com.jpromi.operation_point.repository.UnitRepository;
import com.jpromi.operation_point.service.ApiOperationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ApiOperationServiceImpl implements ApiOperationService {

    @Value("${com.jpromi.operation_point.crawler.tyrol.authentication}")
    private String crawlerTyrolAuthentication;

    @Autowired
    private OperationVariableService operationVariableService;

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private FiredepartmentRepository firedepartmentRepository;

    @Autowired
    private UnitRepository unitRepository;

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
        try {
            // get data from API
            WebClient webClient = WebClient.create();
            ApiOperationLowerAustriaListResponse result = webClient.get()
                    .uri("https://infoscreen.florian10.info/OWS/wastlMobile/getEinsatzAktiv.ashx")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(ApiOperationLowerAustriaListResponse.class)
                    .block();

            // build operation
            List<Operation> operationList = new ArrayList<>();

            result.getEinsatz().forEach( operationListEntry -> {
                try {
                    ApiOperationLowerAustriaResponse operation = getOperationByWastlPubIdLowerAustria(operationListEntry.getI());
                    Operation _operation = updateSavedOperationLowerAustria(operation, operationListEntry.getI());
                    if (_operation != null) {
                        operationList.add(_operation);
                    }

                } catch (Exception e) {
                    System.err.println("Error processing operation: " + operationListEntry.getI() + " - " + e.getMessage());
                }
            });

            checkVanishedOperations(operationList, ServiceOriginEnum.LA_WASTL_PUB);

            return operationList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching operations from Burgenland API", e);
        }
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
        try {
            // get data from API
            WebClient webClient = WebClient.create();
            String json = webClient.get()
                    .uri("https://einsatzuebersicht.lfv.steiermark.at/einsatzkarte/data/public_current.json")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper objectMapper = new ObjectMapper();
            ApiOperationStyriaResponse result = objectMapper.readValue(json, ApiOperationStyriaResponse.class);

            // build operation
            List<Operation> operationList = new ArrayList<>();

            result.getFeatures().forEach( operation -> {
                Operation _operation = updateSavedOperationStyria(operation);
                if (_operation != null) {
                    operationList.add(_operation);
                }
            });

            checkVanishedOperations(operationList, ServiceOriginEnum.ST_LFV_PUB);

            return operationList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching operations from Burgenland API", e);
        }
    }

    @Override
    public List<Operation> getOperationListTyrol() {
        try {
            String authentication = "Basic " + Base64.getEncoder().encodeToString(crawlerTyrolAuthentication.getBytes());
            // get data from API
            WebClient webClient = WebClient.create();
            String json = webClient.get()
                    .uri("https://ffw-einsatzmonitor.at/lfs/proxytirol.php")
                    .accept(MediaType.APPLICATION_JSON)
                    .header("Authorization", authentication)
                    .header("X-Requested-With", "xmlhttprequest")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            ObjectMapper mapper = new ObjectMapper();
            List<ApiOperationTyrolResponse> result = mapper.readValue(json, new TypeReference<List<ApiOperationTyrolResponse>>() {});

            // build operation
            List<Operation> operationList = new ArrayList<>();

            result.forEach( operation -> {
                Operation _operation = updateSavedOperationTyrol(operation);
                if (_operation != null) {
                    operationList.add(_operation);
                }
            });

            checkVanishedOperations(operationList, ServiceOriginEnum.TYROL_LFS_APP);

            return operationList;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching operations from Burgenland API", e);
        }
    }

    private Operation updateSavedOperationBurgenland(ApiOperationBurgenlandResponse.ApiOperationBurgenlandResponseOperation response) {
        if (response.getInfo() == null || !response.getInfo()) {
            Optional<Operation> _operation = operationRepository.findByBlId(response.getOperationId());

            if (_operation.isPresent()) {
                Operation operation = _operation.get();
                operation.setAlarmLevel(operationVariableService.getAlarmLevel(response.getCode()));
                operation.setAlarmType(operationVariableService.getAlarmType(response.getCode()));
                operation.setAlarmLevelAddition(operationVariableService.getAlarmLevelAddition(response.getCode()));
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
                        .alarmLevelAddition(operationVariableService.getAlarmLevelAddition(response.getCode()))
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

    private Operation updateSavedOperationStyria(ApiOperationStyriaResponse.ApiOperationStyriaResponseFeature response) {
        Optional<Operation> _opertaion = operationRepository.findByStId(response.getProperties().getInstanznummer());

        if(_opertaion.isPresent()) {
            String alarmSplit = response.getProperties().getTyp().split("-")[0];
            Operation operation = _opertaion.get();
            operation.setAlarmType(operationVariableService.getAlarmType(alarmSplit));
            operation.setAlarmLevel(operationVariableService.getAlarmLevel(alarmSplit));
            operation.setAlarmLevelAddition(operationVariableService.getAlarmLevelAddition(alarmSplit));
            operation.setAlarmText(response.getProperties().getArt());
            operation.setLat(response.getGeometry().getCoordinates().get(1));
            operation.setLng(response.getGeometry().getCoordinates().get(0));

            // main firedepartment
            Firedepartment mainFiredepartment = createFiredepartmentIfNotExists(
                    Firedepartment.builder().name(response.getProperties().getFeuerwehr()).build()
            );
            List<OperationFiredepartment> firedepartments = operation.getFiredepartments();
            if (firedepartments == null) {
                firedepartments = new ArrayList<>();
            }
            // check if firedepartment already exists
            boolean exists = firedepartments.stream()
                    .anyMatch(opFd -> opFd.getFiredepartment().getName().equals(mainFiredepartment.getName()));
            if (!exists) {
                OperationFiredepartment mainOpFd = OperationFiredepartment.builder()
                        .firedepartment(mainFiredepartment)
                        .operation(operation)
                        .build();
                firedepartments.add(mainOpFd);
            }

            operation.setFiredepartments(firedepartments);

            // end operation
            if (response.getProperties().getWehrenImEinsatz().equals("Abgeschlossen")) {
                operation.setEndTime(OffsetDateTime.now());
            }

            return operationRepository.save(operation);
        } else {
            String alarmSplit = response.getProperties().getTyp().split("-")[0];
            Operation operation = Operation.builder()
                    .stId(response.getProperties().getInstanznummer())
                    .alarmType(operationVariableService.getAlarmType(alarmSplit))
                    .alarmLevel(operationVariableService.getAlarmLevel(alarmSplit))
                    .alarmText(response.getProperties().getArt())
                    .startTime(OffsetDateTime.now())
                    .lat(response.getGeometry().getCoordinates().get(1))
                    .lng(response.getGeometry().getCoordinates().get(0))
                    .serviceOrigin(ServiceOriginEnum.ST_LFV_PUB)
                    .federalState("Styria")
                    .build();

            // firedepartments
            List<OperationFiredepartment> firedepartments = new ArrayList<>();

            // add primary
            Firedepartment mainFiredepartment = createFiredepartmentIfNotExists(
                    Firedepartment.builder().name(response.getProperties().getFeuerwehr()).build()
            );

            OperationFiredepartment mainOpFd = OperationFiredepartment.builder()
                    .firedepartment(mainFiredepartment)
                    .operation(operation)
                    .build();
            firedepartments.add(mainOpFd);

            operation.setFiredepartments(firedepartments);

            // end operation
            if (response.getProperties().getWehrenImEinsatz().equals("Abgeschlossen")) {
                operation.setEndTime(OffsetDateTime.now());
            }

            return operationRepository.save(operation);
        }
    }

    private Operation updateSavedOperationTyrol(ApiOperationTyrolResponse response) {
        Optional<Operation> _operation = operationRepository.findByTyEventId(response.getEventnum());

        if(_operation.isPresent()) {
            Operation operation = _operation.get();
            operation.setAlarmText(response.getRemark());
            operation.setCity(response.getCity());
            operation.setZipCode(response.getZipcode());
            operation.setLocation(response.getCity());
            operation.setLat(response.getLat());
            operation.setLng(response.getLon());
            operation.setUpdatedAt(OffsetDateTime.now());
            operation.setTyAlarmCategory(operationVariableService.getAlarmCategoryTyrol(response.getName()));
            operation.setTyAlarmOrganization(operationVariableService.getAlarmOrganizationTyrol(response.getName()));
            operation.setTyAlarmOutOrder(operationVariableService.getAlarmOutOrderTyrol(response.getName()));

            // firedepartments
            List<String> firedepartments = getFiredepartmentsTyrol(response.getNameAtAlarmTime());
            List<OperationFiredepartment> operationFiredepartments = operation.getFiredepartments();

            firedepartments.forEach(fd -> {
                Firedepartment firedepartment = createFiredepartmentIfNotExists(
                        Firedepartment.builder().name(fd).build()
                );

                // check if firedepartment already exists
                boolean exists = operationFiredepartments.stream()
                        .anyMatch(opFd -> opFd.getFiredepartment().getName().equals(firedepartment.getName()));
                if (!exists) {
                    OperationFiredepartment opFd = OperationFiredepartment.builder()
                            .firedepartment(firedepartment)
                            .operation(operation)
                            .build();
                    operationFiredepartments.add(opFd);
                }
            });

            operation.setFiredepartments(operationFiredepartments);

            // units
            List<String> unitNames = getUnitsTyrol(response.getNameAtAlarmTime());
            List<OperationUnit> operationUnits = operation.getUnits();
            unitNames.forEach(fd -> {
                Unit unit = createUnitIfNotExists(
                        Unit.builder().name(fd).build()
                );

                // check if unit already exists
                boolean exists = operationUnits.stream()
                        .anyMatch(opFd -> opFd.getUnit().getName().equals(unit.getName()));
                if (!exists) {
                    OperationUnit opUn = OperationUnit.builder()
                            .unit(unit)
                            .operation(operation)
                            .build();
                    operationUnits.add(opUn);
                }
            });

            operation.setUnits(operationUnits);

            // end operation
            if (response.getStatus().equals("finished")) {
                operation.setEndTime(OffsetDateTime.now());
            }

            return operationRepository.save(operation);
        } else {
            Operation operation = Operation.builder()
                    .tyEventId(response.getEventnum())
                    .alarmText(response.getRemark())
                    .tyAlarmCategory(operationVariableService.getAlarmCategoryTyrol(response.getName()))
                    .tyAlarmOrganization(operationVariableService.getAlarmOrganizationTyrol(response.getName()))
                    .tyAlarmOutOrder(operationVariableService.getAlarmOutOrderTyrol(response.getName()))
                    .city(response.getCity())
                    .zipCode(response.getZipcode())
                    .location(response.getCity())
                    .lat(response.getLat())
                    .lng(response.getLon())
                    .startTime(OffsetDateTime.now())
                    .serviceOrigin(ServiceOriginEnum.TYROL_LFS_APP)
                    .federalState("Tyrol")
                    .build();
            // firedepartments
            List<OperationFiredepartment> firedepartments = new ArrayList<>();
            List<String> firedepartmentNames = getFiredepartmentsTyrol(response.getNameAtAlarmTime());
            firedepartmentNames.forEach(firedepartmentName -> {
                Firedepartment firedepartment = createFiredepartmentIfNotExists(
                        Firedepartment.builder().name(firedepartmentName).build()
                );

                OperationFiredepartment opFd = OperationFiredepartment.builder()
                        .firedepartment(firedepartment)
                        .operation(operation)
                        .build();
                firedepartments.add(opFd);
            });
            operation.setFiredepartments(firedepartments);

            // units
            List<OperationUnit> units = new ArrayList<>();
            List<String> unitNames = getUnitsTyrol(response.getNameAtAlarmTime());
            unitNames.forEach(unitName -> {
                OperationUnit unit = OperationUnit.builder()
                        .operation(operation)
                        .unit(createUnitIfNotExists(Unit.builder().name(unitName).build()))
                        .build();
                units.add(unit);
            });
            operation.setUnits(units);
            return operationRepository.save(operation);
        }
    }

    private Operation updateSavedOperationLowerAustria(ApiOperationLowerAustriaResponse response, String laWastlPubId) {
        Optional<Operation> _operation = operationRepository.findByLaSysId(response.getN());

        if(_operation.isPresent()) {
            Operation operation = _operation.get();
            operation.setAlarmType(operationVariableService.getAlarmType(response.getA()));
            operation.setAlarmLevel(operationVariableService.getAlarmLevel(response.getA()));
            operation.setAlarmText(response.getM());
            operation.setStartTime(operationVariableService.getTimeFromDateAndTime(response.getD(), response.getT()));
            operation.setLocation(response.getO());
            operation.setCity(response.getO());
            operation.setZipCode(response.getP());

            // firedepartments / units, and check if they already exist
            List<OperationFiredepartment> firedepartments = operation.getFiredepartments();
            List<OperationUnit> units = operation.getUnits();
            if (firedepartments == null) {
                firedepartments = new ArrayList<>();
            } else {
                firedepartments = operation.getFiredepartments();
            }
            if (units == null) {
                units = new ArrayList<>();
            }
            List<OperationUnit> finalUnits = units;
            List<OperationFiredepartment> finalFiredepartments = firedepartments;
            response.getDispo().forEach(dispo -> {
                if(isUnitLowerAustria(dispo.getN())) {
                    // unit
                    Unit unit = createUnitIfNotExists(
                            Unit.builder().name(dispo.getN()).build()
                    );
                    // check if unit already exists
                    boolean exists = finalUnits.stream()
                            .anyMatch(opUn -> opUn.getUnit().getName().equals(unit.getName()));
                    if (!exists) {
                        OperationUnit operationUnit = OperationUnit.builder()
                                .unit(unit)
                                .operation(operation)
                                .dispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()))
                                .alarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()))
                                .outTime(operationVariableService.getFromAnonymousTime(dispo.getOt()))
                                .inTime(operationVariableService.getFromAnonymousTime(dispo.getIt()))
                                .build();
                        finalUnits.add(operationUnit);
                    } else {
                        // update existing unit
                        OperationUnit existingUnit = finalUnits.stream()
                                .filter(opUn -> opUn.getUnit().getName().equals(unit.getName()))
                                .findFirst()
                                .orElse(null);
                        if (existingUnit != null) {
                            existingUnit.setDispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()));
                            existingUnit.setAlarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()));
                            existingUnit.setOutTime(operationVariableService.getFromAnonymousTime(dispo.getOt()));
                            existingUnit.setInTime(operationVariableService.getFromAnonymousTime(dispo.getIt()));
                        }
                    }
                } else {
                    // firedepartment
                    Firedepartment firedepartment = createFiredepartmentIfNotExists(
                            Firedepartment.builder().name(dispo.getN()).build()
                    );
                    // check if unit already exists
                    boolean exists = finalFiredepartments.stream()
                            .anyMatch(opUn -> opUn.getFiredepartment().getName().equals(firedepartment.getName()));
                    if (!exists) {
                        OperationFiredepartment operationFiredepartment = OperationFiredepartment.builder()
                                .firedepartment(firedepartment)
                                .operation(operation)
                                .dispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()))
                                .alarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()))
                                .outTime(operationVariableService.getFromAnonymousTime(dispo.getOt()))
                                .inTime(operationVariableService.getFromAnonymousTime(dispo.getIt()))
                                .build();
                        finalFiredepartments.add(operationFiredepartment);
                    } else {
                        // update existing firedepartment
                        OperationFiredepartment existingUnit = finalFiredepartments.stream()
                                .filter(opUn -> opUn.getFiredepartment().getName().equals(firedepartment.getName()))
                                .findFirst()
                                .orElse(null);
                        if (existingUnit != null) {
                            existingUnit.setDispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()));
                            existingUnit.setAlarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()));
                            existingUnit.setOutTime(operationVariableService.getFromAnonymousTime(dispo.getOt()));
                            existingUnit.setInTime(operationVariableService.getFromAnonymousTime(dispo.getIt()));
                        }
                    }
                }
            });

            operation.setFiredepartments(finalFiredepartments);
            operation.setUnits(finalUnits);

            operation.setUpdatedAt(OffsetDateTime.now());

            return operationRepository.save(operation);
        } else {
            Operation operation = Operation.builder()
                    .laSysId(response.getN())
                    .laWastlPubId(laWastlPubId)
                    .laId(response.getId())

                    .alarmText(response.getM())
                    .alarmType(operationVariableService.getAlarmType(response.getA()))
                    .alarmLevel(operationVariableService.getAlarmLevel(response.getA()))
                    .startTime(operationVariableService.getTimeFromDateAndTime(response.getD(), response.getT()))
                    .location(response.getO())
                    .city(response.getO())
                    .zipCode(response.getP())
                    .federalState("Lower Austria")
                    .serviceOrigin(ServiceOriginEnum.LA_WASTL_PUB)
                    .build();

            // firedepartments / units
            List<OperationFiredepartment> firedepartments = new ArrayList<>();
            List<OperationUnit> units = new ArrayList<>();

            response.getDispo().forEach(dispo -> {
                if(isUnitLowerAustria(dispo.getN())) {
                    // unit
                    Unit unit = createUnitIfNotExists(
                            Unit.builder().name(dispo.getN()).build()
                    );
                    OperationUnit operationUnit = OperationUnit.builder()
                            .unit(unit)
                            .operation(operation)
                            .dispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()))
                            .alarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()))
                            .outTime(operationVariableService.getFromAnonymousTime(dispo.getOt()))
                            .inTime(operationVariableService.getFromAnonymousTime(dispo.getIt()))
                            .build();
                    units.add(operationUnit);
                } else {
                    // firedepartment
                    Firedepartment firedepartment = createFiredepartmentIfNotExists(
                            Firedepartment.builder().name(dispo.getN()).build()
                    );

                    OperationFiredepartment opFd = OperationFiredepartment.builder()
                            .firedepartment(firedepartment)
                            .operation(operation)
                            .dispoTime(operationVariableService.getFromAnonymousTime(dispo.getDt()))
                            .alarmTime(operationVariableService.getFromAnonymousTime(dispo.getAt()))
                            .outTime(operationVariableService.getFromAnonymousTime(dispo.getOt()))
                            .inTime(operationVariableService.getFromAnonymousTime(dispo.getIt()))
                            .build();
                    firedepartments.add(opFd);
                }
            });
            operation.setFiredepartments(firedepartments);
            operation.setUnits(units);

            return operationRepository.save(operation);
        }
    }

    private ApiOperationLowerAustriaResponse getOperationByWastlPubIdLowerAustria(String laWastlPubId) {
        if(laWastlPubId != null) {
            try {
                // get data from API
                WebClient webClient = WebClient.create();
                ResponseEntity<ApiOperationLowerAustriaResponse> response = webClient.get()
                        .uri("https://infoscreen.florian10.info/OWS/wastlMobile/geteinsatzdata.ashx?id=" + laWastlPubId)
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .toEntity(ApiOperationLowerAustriaResponse.class)
                        .block();

                return response.getBody();
            } catch (Exception e) {
                throw new RuntimeException("Error fetching operation by ID from Lower Austria API", e);
            }
        } else {
            throw new IllegalArgumentException("laWastlPubId cannot be null");
        }

    }


    private Firedepartment createFiredepartmentIfNotExists(Firedepartment firedepartment) {
        if (firedepartment.getFriendlyName() == null || firedepartment.getFriendlyName().isEmpty()) {
            firedepartment.setFriendlyName(firedepartment.getName());
        }
        Optional<Firedepartment> existing = firedepartmentRepository.findByName(firedepartment.getName());
        return existing.orElseGet(() -> firedepartmentRepository.save(firedepartment));
    }

    private Unit createUnitIfNotExists(Unit unit) {
        if (unit.getFriendlyName() == null || unit.getFriendlyName().isEmpty()) {
            unit.setFriendlyName(unit.getName());
        }
        Optional<Unit> existing = unitRepository.findByName(unit.getName());
        return existing.orElseGet(() -> unitRepository.save(unit));
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

    private List<String> getFiredepartmentsTyrol(List<String> names) {
        List<String> firedepartments = new ArrayList<>();
        for (String name : names) {
            if (name.endsWith("Florian")) {
                String cleanedName = name.substring(0, name.length() - 8).trim();
                firedepartments.add(name);
            }
        }

        return firedepartments;
    }

    private List<String> getUnitsTyrol(List<String> names) {
        List<String> units = new ArrayList<>();
        for (String name : names) {
            if (!name.startsWith("FF")) {
                units.add(name);
            }
        }

        return units;
    }

    private Boolean isUnitLowerAustria(String name) {
        Optional<Unit> unit = unitRepository.findByName(name);

        // check if exists as unit
        if (unit.isPresent()) {
            return true;
        } else {
            Optional<Firedepartment> firedepartment = firedepartmentRepository.findByName(name);
            // check if exists as firedepartment
            if (firedepartment.isPresent()) {
                return false;
            } else {
                // check with name pattern
                if (name.contains("(") && name.contains(")")) {
                    String unitName = name.substring(name.indexOf("(") + 1, name.indexOf(")")).trim();
                    return !(unitName.equals("FF") || unitName.equals("FW"));
                } else {
                    return false;
                }
            }
        }
    }

}

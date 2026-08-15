package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.entity.Operation;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import com.jpromi.operation_point.service.LocationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class LocationStatisticResponseMapper {

    private final OperationVariableService operationVariableService;
    private final LocationService locationService;

    public LocationStatisticResponseMapper(OperationVariableService operationVariableService, LocationService locationService) {
        this.operationVariableService = operationVariableService;
        this.locationService = locationService;
    }

    public List<LocationStatisticResponse> fromOperationFederalState(List<Operation> operations) {
        Map<String, List<Operation>> grouped = operations.stream()
                .filter(op -> op.getFederalState() != null)
                .collect(Collectors.groupingBy(Operation::getFederalState));

        return grouped.entrySet().stream()
                .map(entry -> {
                    String federalState = entry.getKey();
                    List<Operation> ops = entry.getValue();

                    long count = ops.size();
                    long countFire = 0;
                    long countTechnical = 0;
                    long countAcid = 0;
                    long countOther = 0;

                    for (Operation op : ops) {
                        if(op.getFederalState().equals("Tyrol")) {
                            if (op.getTyAlarmCategory() != null) {
                                switch (op.getTyAlarmCategory()) {
                                    case "BRANDG", "BRANDK", "EXPLOSION":
                                        countFire++;
                                        break;
                                    case "TECHNIK", "VERKEHR", "EINSTURZ", "BAHN", "FLUG", "STROM", "UNTERSTÜTZ":
                                        countTechnical++;
                                        break;
                                    case "ÖL", "ABC", "GAS", "WASSER":
                                        countAcid++;
                                        break;
                                    default:
                                        countOther++;
                                        break;
                                }
                            } else {
                                countOther++;
                            }
                        } else {
                            if (op.getAlarmType() != null) {
                                switch (op.getAlarmType()) {
                                    case "B":
                                        countFire++;
                                        break;
                                    case "T":
                                    case "V":
                                    case "KL":
                                        countTechnical++;
                                        break;
                                    case "G":
                                    case "S":
                                        countAcid++;
                                        break;
                                    default:
                                        countOther++;
                                        break;
                                }
                            }
                        }
                    }

                    LocationStatisticResponse response = new LocationStatisticResponse();
                    response.setNameId(getFederalStateId(federalState));
                    response.setCountActive(count);
                    response.setCountFire(countFire);
                    response.setCountTechnical(countTechnical);
                    response.setCountAcid(countAcid);
                    response.setCountOther(countOther);

                    return response;
                })
                .collect(Collectors.toList());
    }

    public List<LocationStatisticResponse> fromOperationDistrict(
            List<Operation> operations,
            String federalState
    ) {
        String resolvedFederalState =
                operationVariableService.getFederalState(federalState);

        Map<String, List<Operation>> grouped = operations.stream()
                .filter(op -> Objects.equals(
                        op.getFederalState(),
                        resolvedFederalState
                ))
                .filter(op -> op.getDistrict() != null)
                .collect(Collectors.groupingBy(Operation::getDistrict));

        List<LocationStatisticResponse> result = grouped.entrySet().stream()
                .map(entry -> createStatistic(
                        entry.getKey(),
                        entry.getValue()
                ))
                .sorted(Comparator.nullsLast(Comparator.comparing(LocationStatisticResponse::getNameId)))
                .collect(Collectors.toCollection(ArrayList::new));

        LocationStatisticResponse all = new LocationStatisticResponse();

        // Recalculate statistics for all from all operations
        long countFireAll = 0;
        long countTechnicalAll = 0;
        long countAcidAll = 0;
        long countOtherAll = 0;

        for (Operation operation : operations) {
            String category = getCategory(operation);

            switch (category) {
                case "FIRE" -> countFireAll++;
                case "TECHNICAL" -> countTechnicalAll++;
                case "ACID" -> countAcidAll++;
                case "OTHER" -> countOtherAll++;
            }
        }

        all.setNameId("all");
        all.setCountActive((long) operations.size());
        all.setCountFire(countFireAll);
        all.setCountTechnical(countTechnicalAll);
        all.setCountAcid(countAcidAll);
        all.setCountOther(countOtherAll);

        result.addFirst(all);

        return result;
    }

    private String getFederalStateId(String federalState) {
        switch (federalState) {
            case "Burgenland":
                return "bl";
            case "Carinthia":
                return "ct";
            case "Lower Austria":
                return "la";
            case "Upper Austria":
                return "ua";
            case "Salzburg":
                return "sb";
            case "Styria":
                return "st";
            case "Tyrol":
                return "ty";
            case "Vorarlberg":
                return "vb";
            default:
                return null;
        }
    }

    private LocationStatisticResponse createStatistic(
            String district,
            List<Operation> operations
    ) {
        long countFire = 0;
        long countTechnical = 0;
        long countAcid = 0;
        long countOther = 0;

        for (Operation operation : operations) {
            String category = getCategory(operation);

            switch (category) {
                case "FIRE" -> countFire++;
                case "TECHNICAL" -> countTechnical++;
                case "ACID" -> countAcid++;
                case "OTHER" -> countOther++;
            }
        }

        LocationStatisticResponse response = new LocationStatisticResponse();

        try {
            String districtId = locationService.getDistrictIdByDistrict(district);
            response.setNameId(districtId != null ? districtId : district);
        } catch (IllegalArgumentException e) {
            response.setNameId(district);
        }

        response.setCountActive((long) operations.size());
        response.setCountFire(countFire);
        response.setCountTechnical(countTechnical);
        response.setCountAcid(countAcid);
        response.setCountOther(countOther);

        return response;
    }

    private String getCategory(Operation operation) {
        if ("Tyrol".equals(operation.getFederalState())) {
            return switch (operation.getTyAlarmCategory()) {
                case "BRANDG", "BRANDK", "EXPLOSION" ->
                        "FIRE";

                case "TECHNIK", "VERKEHR", "EINSTURZ",
                     "BAHN", "FLUG", "STROM", "UNTERSTÜTZ" ->
                        "TECHNICAL";

                case "ÖL", "ABC", "GAS", "WASSER" ->
                        "ACID";

                case null, default ->
                        "OTHER";
            };
        }

        return switch (operation.getAlarmType()) {
            case "B" ->
                    "FIRE";

            case "T", "V", "KL" ->
                    "TECHNICAL";

            case "G", "S" ->
                    "ACID";

            case null, default ->
                    "OTHER";
        };
    }

}

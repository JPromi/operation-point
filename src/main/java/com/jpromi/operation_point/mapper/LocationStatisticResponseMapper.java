package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.enums.ServiceOriginEnum;
import com.jpromi.operation_point.model.LocationStatisticResponse;
import com.jpromi.operation_point.service.LocationService;
import com.jpromi.operation_point.service.OperationVariableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LocationStatisticResponseMapper {

    @Autowired
    private OperationVariableService operationVariableService;

    @Autowired
    private LocationService locationService;

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

    public List<LocationStatisticResponse> fromOperationDistrict(List<Operation> operations, String federalState) {
        Map<String, List<Operation>> grouped = operations.stream()
                .filter(op -> op.getFederalState() != null && op.getFederalState().equals(operationVariableService.getFederalState(federalState)))
                .filter(op -> op.getDistrict() != null)
                .collect(Collectors.groupingBy(Operation::getDistrict));


        return grouped.entrySet().stream()
                .map(entry -> {
                    String district = entry.getKey();
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

                    try {
                        response.setNameId(locationService.getDistrictIdByDistrict(district));
                    } catch (IllegalArgumentException e) {
                        response.setNameId(district);
                    }
                    response.setCountActive(count);
                    response.setCountFire(countFire);
                    response.setCountTechnical(countTechnical);
                    response.setCountAcid(countAcid);
                    response.setCountOther(countOther);

                    return response;
                })
                .collect(Collectors.toList());
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


}

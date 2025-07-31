package com.jpromi.operation_point.mapper;

import com.jpromi.operation_point.enitiy.Operation;
import com.jpromi.operation_point.model.FiredepartmentLightResponse;
import com.jpromi.operation_point.model.OperationResponse;
import com.jpromi.operation_point.model.UnitLightResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OperationResponseMapper {

    public OperationResponse fromOperation(Operation operation) {
        OperationResponse response = OperationResponse.builder()
                .uuid(operation.getUuid())
                .startTime(operation.getStartTime())
                .endTime(operation.getEndTime())
                .build();

        response.getAlarm().setLevel(operation.getAlarmLevel());
        response.getAlarm().setType(operation.getAlarmType());
        response.getAlarm().setLevelAddition(operation.getAlarmLevelAddition());
        response.getAlarm().setMessage(operation.getAlarmText());
        response.getAlarm().setUpperAustriaId(operation.getUaAlarmTypeId());
        response.getAlarm().setUpperAustriaType(operation.getUaAlarmTypeType());
        response.getAlarm().setTyrolOrganization(operation.getTyAlarmOrganization());
        response.getAlarm().setTyrolOutOrder(operation.getTyAlarmOutOrder());
        response.getAlarm().setTyrolCategory(operation.getTyAlarmCategory());

        response.getExternalIds().setUpperAustriaId(operation.getUaId());
        response.getExternalIds().setTyrolEventId(operation.getTyEventId());
        response.getExternalIds().setBurgenlandId(operation.getBlId());
        response.getExternalIds().setStyriaId(operation.getStId());
        response.getExternalIds().setLowerAustriaWastlPubId(operation.getLaWastlPubId());
        response.getExternalIds().setLowerAustriaSysId(operation.getLaSysId());
        response.getExternalIds().setLowerAustriaId(operation.getLaId());

        response.getAddress().setCountry(operation.getCountry());
        response.getAddress().setFederalState(operation.getFederalState());
        response.getAddress().setCity(operation.getCity());
        response.getAddress().setZipCode(operation.getZipCode());
        response.getAddress().setDistrict(operation.getDistrict());
        response.getAddress().setLocation(operation.getLocation());

        response.getSystem().setServiceOrigin(operation.getServiceOrigin());
        response.getSystem().setFirstSeen(operation.getFirstSeen());
        response.getSystem().setLastSeen(operation.getLastSeen());
        response.getSystem().setLastUpdate(operation.getUpdatedAt());

        if (operation.getFiredepartments() != null) {
            List<OperationResponse.OperationResponseFiredepartment> firedepartments = new ArrayList<>();

            operation.getFiredepartments().forEach(fd -> {
                firedepartments.add(
                        OperationResponse.OperationResponseFiredepartment.builder()
                                .firedepartment(
                                        fd.getFiredepartment() != null ? FiredepartmentLightResponse.builder()
                                                .uuid(fd.getFiredepartment().getUuid())
                                                .name(fd.getFiredepartment().getFriendlyName())
                                                .atFireDepartmentId(fd.getFiredepartment().getAtFireDepartmentId())
                                                .isVolunteer(fd.getFiredepartment().getIsVolunteer())
                                                .build() : null
                                )
                                .alarmTime(fd.getAlarmTime())
                                .dispoTime(fd.getDispoTime())
                                .inTime(fd.getInTime())
                                .outTime(fd.getOutTime())
                                .build()
                );
            });

            response.setFiredepartments(firedepartments);
        }

        if(operation.getUnits() != null) {
            List<OperationResponse.OperationResponseUnit> units = new ArrayList<>();

            operation.getUnits().forEach(unit -> {
                units.add(
                        OperationResponse.OperationResponseUnit.builder()
                                .unit(
                                        unit.getUnit() != null ? UnitLightResponse.builder()
                                                .uuid(unit.getUnit().getUuid())
                                                .name(unit.getUnit().getFriendlyName())
                                                .build() : null
                                )
                                .dispoTime(unit.getDispoTime())
                                .outTime(unit.getOutTime())
                                .inTime(unit.getInTime())
                                .build()
                );
            });

            response.setUnits(units);
        }


        return response;
    }
}

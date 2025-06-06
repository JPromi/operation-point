package com.jpromi.operation_point.service;

import java.time.OffsetDateTime;

public interface OperationVariableService {
    OffsetDateTime getTimeFromDateAndTime(String date, String time);
    String getAlarmType(String alarmType);
    Long getAlarmLevel(String code);
    String getAlarmOrganizationTyrol(String alarm);
    String getAlarmOutOrderTyrol(String alarm);
    String getAlarmCategoryTyrol(String alarm);
    OffsetDateTime getFromAnonymousTime(String datetime);
}
